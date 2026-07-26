package com.tms.appcliente.bootstrap.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tms.appcliente.shared.web.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/** Equivalente a {@link JsonAuthenticationEntryPoint} pero para 403 (RBAC insuficiente). */
@Component
public class JsonAccessDeniedHandler implements AccessDeniedHandler {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                        AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ApiResponse<Void> body = ApiResponse.error("No cuenta con permisos suficientes para esta operación.");
        OBJECT_MAPPER.writeValue(response.getWriter(), body);
    }
}

