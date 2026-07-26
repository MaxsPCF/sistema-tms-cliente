package com.tms.appcliente.bootstrap.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tms.appcliente.shared.web.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Los rechazos que ocurren DENTRO de la cadena de filtros de Spring Security
 * (token ausente/expirado en un endpoint protegido) nunca llegan al
 * {@code GlobalExceptionHandler} de shared-kernel (ese solo aplica a
 * excepciones lanzadas durante la ejecución del controlador). Este
 * EntryPoint asegura que incluso esos 401 respeten el mismo sobre universal
 * de respuesta {success, data, message, errors} (requerimiento B).
 */
@Component
public class JsonAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                          AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ApiResponse<Void> body = ApiResponse.error("Se requiere autenticación (token ausente, inválido o expirado).");
        OBJECT_MAPPER.writeValue(response.getWriter(), body);
    }
}

