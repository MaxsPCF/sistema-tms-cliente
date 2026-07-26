package com.tms.appcliente.shared.web;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Envoltorio universal de respuestas de la API (requerimiento B).
 * Todo controlador debe, directa o indirectamente (vía {@link ApiResponseAdvice}),
 * responder bajo esta forma:
 *
 * <pre>
 * {
 *   "success": true,
 *   "data": { ... },
 *   "message": "Mensaje informativo opcional",
 *   "errors": []
 * }
 * </pre>
 */
@Schema(description = "Envoltorio universal de respuestas de la API")
public record ApiResponse<T>(
        @Schema(description = "Indica si la operación fue exitosa", example = "true")
        boolean success,

        @Schema(description = "Datos de la respuesta")
        T data,

        @Schema(description = "Mensaje descriptivo de la operación", example = "Operación exitosa")
        String message,

        @Schema(description = "Lista de errores si la operación falló")
        List<String> errors,

        @Schema(description = "Timestamp de la respuesta")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        LocalDateTime timestamp,

        @Schema(description = "ID de trazabilidad de la petición")
        String traceId
        ) {

    // Constructor canónico con valores por defecto
    public ApiResponse {
        if (errors == null) errors = Collections.emptyList();
        if (timestamp == null) timestamp = LocalDateTime.now();
        if (traceId == null) traceId = generateTraceId();
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, null, List.of(), LocalDateTime.now(), generateTraceId());
    }

    public static <T> ApiResponse<T> ok(T data, String message) {
        return new ApiResponse<>(true, data, message, List.of(), LocalDateTime.now(), generateTraceId());
    }

    public static <T> ApiResponse<T> error(String message, List<String> errors) {
        return new ApiResponse<>(false, null, message, errors, LocalDateTime.now(), generateTraceId());
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, null, message, List.of(), LocalDateTime.now(), generateTraceId());
    }

    private static String generateTraceId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}

