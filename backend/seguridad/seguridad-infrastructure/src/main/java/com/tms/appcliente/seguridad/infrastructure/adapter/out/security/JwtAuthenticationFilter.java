package com.tms.appcliente.seguridad.infrastructure.adapter.out.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Filtro stateless (requerimiento OpenAPI/JWT): valida el Bearer token en cada
 * request y, si es válido, puebla el SecurityContext con las authorities
 * "ROLE_x" derivadas del claim "roles" — de ahí en adelante RBAC se resuelve
 * declarativamente con @PreAuthorize en los controladores.
 * No mantiene sesión (SessionCreationPolicy.STATELESS, ver SecurityConfig).
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private static final String PREFIJO_BEARER = "Bearer ";

    private final JwtTokenProvider tokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith(PREFIJO_BEARER)) {
            String token = header.substring(PREFIJO_BEARER.length());
            try {
                Claims claims = tokenProvider.parsear(token);
                @SuppressWarnings("unchecked")
                List<String> roles = claims.get("roles", List.class);
                List<GrantedAuthority> authorities = (roles == null ? List.<String>of() : roles).stream()
                        .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol))
                        .map(GrantedAuthority.class::cast)
                        .toList();

                var authentication = new UsernamePasswordAuthenticationToken(
                        claims.getSubject(), null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (JwtException | IllegalArgumentException ex) {
                // Token inválido/expirado/manipulado: no se autentica y se continúa
                // la cadena; el endpoint protegido responderá 401/403 según corresponda.
                // Nunca se revela el motivo exacto al cliente (evita oracle de ataque).
                log.debug("Token JWT rechazado: {}", ex.getMessage());
                SecurityContextHolder.clearContext();
            }
        }
        chain.doFilter(request, response);
    }
}

