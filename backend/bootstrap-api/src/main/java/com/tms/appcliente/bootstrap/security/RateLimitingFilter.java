package com.tms.appcliente.bootstrap.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tms.appcliente.shared.web.ApiResponse;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Mitigación OWASP: fuerza bruta / credential stuffing / abuso de endpoints
 * públicos (A07 - Identification & Authentication Failures) mediante un
 * algoritmo de token bucket (Bucket4j), aplicado por IP de cliente antes de
 * llegar a autenticación o a cualquier controlador.
 *
 * LIMITACIÓN CONOCIDA: el mapa en memoria solo funciona correctamente con una
 * única instancia. En despliegues horizontales (varios pods/instancias) debe
 * sustituirse por un backend distribuido (bucket4j-redis / bucket4j-hazelcast)
 * para que el límite sea consistente entre instancias.
 */
@Component
@Order(1)
@EnableConfigurationProperties(RateLimitProperties.class)
public class RateLimitingFilter extends OncePerRequestFilter {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final RateLimitProperties properties;
    private final ConcurrentHashMap<String, Bucket> bucketsPorIp = new ConcurrentHashMap<>();

    public RateLimitingFilter(RateLimitProperties properties) {
        this.properties = properties;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String clienteIp = resolverIpCliente(request);
        Bucket bucket = bucketsPorIp.computeIfAbsent(clienteIp, ip -> nuevoBucket());

        if (bucket.tryConsume(1)) {
            chain.doFilter(request, response);
            return;
        }

        response.setStatus(429); // 429 Too Many Requests
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ApiResponse<Void> body = ApiResponse.error("Demasiadas solicitudes. Intente nuevamente en unos minutos.");
        OBJECT_MAPPER.writeValue(response.getWriter(), body);
    }

    private Bucket nuevoBucket() {
        return Bucket.builder()
                .addLimit(limit -> limit
                        .capacity(properties.getCapacidad())
                        .refillGreedy(properties.getRefillPorMinuto(), Duration.ofMinutes(1)))
                .build();
    }

    private String resolverIpCliente(HttpServletRequest request) {
        // X-Forwarded-For solo debe confiarse si la API está detrás de un
        // reverse proxy/load balancer de confianza que lo sobrescribe (evita
        // spoofing del header para evadir el límite).
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}

