package com.tms.appcliente.shared.web;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * Aplica el requerimiento B (Envoltorio Universal) de forma transversal: cualquier
 * controlador puede devolver directamente su DTO/record de dominio y este advice
 * lo envuelve automáticamente en {@link ApiResponse}, sin acoplar cada endpoint a
 * construir el sobre manualmente. Si el controlador ya devuelve un ApiResponse
 * (por ejemplo, para adjuntar un mensaje personalizado) se respeta tal cual.
 */
@ControllerAdvice
public class ApiResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(@NonNull MethodParameter returnType,
                            @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        // Solo envolvemos controladores propios. Librerías de terceros (springdoc,
        // actuator, etc.) publican sus propios contratos JSON que NO deben
        // reformatearse, o rompen sus clientes (p. ej. swagger-ui espera
        // {configUrl, oauth2RedirectUrl} tal cual en /v3/api-docs/swagger-config).
        Class<?> declaringClass = returnType.getDeclaringClass();
        String packageName = declaringClass.getPackageName();
        return packageName.startsWith("com.tms.appcliente");
    }

    @Override
    @Nullable
    public Object beforeBodyWrite(@Nullable Object body,
                                   @NonNull MethodParameter returnType,
                                   @NonNull MediaType selectedContentType,
                                   @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                   @NonNull ServerHttpRequest request,
                                   @NonNull ServerHttpResponse response) {
        if (!MediaType.APPLICATION_JSON.isCompatibleWith(selectedContentType)) {
            return body;
        }
        if (body instanceof ApiResponse<?>) {
            return body;
        }
        if (body instanceof String) {
            // Spring serializa Strings vía StringHttpMessageConverter; evitamos
            // conflictos de tipo devolviendo el envoltorio ya serializado no aplica
            // aquí, así que dejamos pasar tal cual (caso raro en esta API JSON-only).
            return body;
        }
        return ApiResponse.ok(body);
    }
}

