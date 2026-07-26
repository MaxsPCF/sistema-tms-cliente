package com.tms.appcliente.bootstrap.config;

import com.tms.appcliente.bootstrap.security.JsonAccessDeniedHandler;
import com.tms.appcliente.bootstrap.security.JsonAuthenticationEntryPoint;
import com.tms.appcliente.bootstrap.security.RateLimitingFilter;
import com.tms.appcliente.seguridad.infrastructure.adapter.out.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Composition root de seguridad transversal (requerimiento 4). Ensambla en un
 * único SecurityFilterChain los endpoints públicos y protegidos de AMBOS
 * módulos de negocio, ya que esa decisión (qué es público/privado) es una
 * responsabilidad de la aplicación completa, no de un módulo aislado.
 * <p>
 * Decisiones de diseño / mitigaciones OWASP:
 * - STATELESS (sin JSESSIONID): elimina fijación/robo de sesión (A01, A07).
 * - CSRF deshabilitado: solo es necesario para autenticación basada en
 * cookies; con Bearer JWT en header no aplica (no hay cookie que un sitio
 * malicioso pueda "arrastrar" automáticamente).
 * - RateLimitingFilter ANTES de la autenticación: corta fuerza bruta antes
 * de gastar ciclos de verificación de contraseña (A07).
 * - JwtAuthenticationFilter ANTES de UsernamePasswordAuthenticationFilter:
 * puebla el SecurityContext por token en cada request (A07).
 * - @EnableMethodSecurity + @PreAuthorize en los controladores: RBAC
 * declarativo y auditable en el punto de uso (A01 - Broken Access Control).
 * - Cabeceras de seguridad explícitas: HSTS, X-Content-Type-Options,
 * frameOptions DENY (A05 - Security Misconfiguration).
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private static final String[] ENDPOINTS_PUBLICOS = {
            "/api/v1/authenticate/login",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/actuator/health",
            "/actuator/info"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtAuthenticationFilter jwtAuthenticationFilter,
                                                   RateLimitingFilter rateLimitingFilter,
                                                   JsonAuthenticationEntryPoint authenticationEntryPoint,
                                                   JsonAccessDeniedHandler accessDeniedHandler) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Bearer JWT stateless: sin cookies, sin CSRF aplicable.
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(ENDPOINTS_PUBLICOS).permitAll()
                        .anyRequest().authenticated())
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::deny)
                        .contentTypeOptions(contentTypeOptions -> {
                        })
                        .httpStrictTransportSecurity(hsts -> hsts
                                .includeSubDomains(true)
                                .maxAgeInSeconds(31536000)))
                .addFilterBefore(rateLimitingFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * CORS restrictivo por defecto: solo orígenes explícitamente configurados
     * (nunca "*") cuando se envían credenciales/Authorization header
     * (mitigación OWASP A05). Ajustar allowedOrigins vía variable de entorno
     * por ambiente (dev/qa/prod) en un despliegue real.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost:5173",
                // "http://localhost:3000",
                "https://app.tms-cliente-transporte.com"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

