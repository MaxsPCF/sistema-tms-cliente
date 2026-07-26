package com.tms.appcliente.seguridad.infrastructure.adapter.out.security;

import com.tms.appcliente.seguridad.application.port.out.TokenProviderPort;
import com.tms.appcliente.seguridad.domain.model.Rol;
import com.tms.appcliente.seguridad.domain.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

/** Adaptador de salida: emisión y parseo de JWT (stateless authentication). */
@Component
@EnableConfigurationProperties(JwtProperties.class)
public class JwtTokenProvider implements TokenProviderPort {

    private final JwtProperties properties;
    private final SecretKey signingKey;

    public JwtTokenProvider(JwtProperties properties) {
        this.properties = properties;
        this.signingKey = Keys.hmacShaKeyFor(properties.getSecret().getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }

    @Override
    public TokenEmitido generar(Usuario usuario, List<Rol> roles) {
        Instant ahora = Instant.now();
        Instant expiracion = ahora.plus(properties.getExpiracionMinutos(), ChronoUnit.MINUTES);

        List<String> nombresRoles = roles.stream().map(Rol::nombreRol).toList();

        String token = Jwts.builder()
                .subject(usuario.idUsuario().toString())
                .issuer(properties.getEmisor())
                .claim("nombreUsuario", usuario.nombreUsuario())
                .claim("email", usuario.email())
                .claim("roles", nombresRoles)
                .issuedAt(Date.from(ahora))
                .expiration(Date.from(expiracion))
                .signWith(signingKey)
                .compact();

        return new TokenEmitido(token, OffsetDateTime.ofInstant(expiracion, ZoneOffset.UTC));
    }

    /** Usado por el filtro de autenticación para validar y extraer claims del token entrante. */
    public Claims parsear(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

