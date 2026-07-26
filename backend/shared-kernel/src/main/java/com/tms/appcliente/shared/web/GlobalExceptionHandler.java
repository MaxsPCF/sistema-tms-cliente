package com.tms.appcliente.shared.web;

import com.tms.appcliente.shared.exception.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * Traduce toda excepción (dominio, validación, seguridad) al envoltorio universal
 * de error (requerimiento B), evitando que cada controlador tenga que hacer su
 * propio manejo. Este es el único punto donde una excepción de negocio "cruza"
 * hacia el formato HTTP; el dominio y la aplicación nunca conocen HttpStatus.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApiResponse<Void>> handleApplicationException(ApplicationException ex) {
        log.warn("Excepción de negocio [{}]: {}", ex.codigo(), ex.getMessage());
        return ResponseEntity.status(ex.httpStatus())
                .body(ApiResponse.error(ex.getMessage(), List.of(ex.codigo())));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
        List<String> errores = ex.getBindingResult().getFieldErrors().stream()
                .map(this::formatearError)
                .toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Error de validación en los datos enviados.", errores));
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<ApiResponse<Void>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("Usuario o contraseña incorrectos."));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error("No cuenta con permisos suficientes para esta operación."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpected(Exception ex) {
        // Deliberadamente no se expone ex.getMessage() al cliente (evita fuga de
        // detalles internos / stack traces -> mitigación OWASP A05/A09).
        log.error("Error interno no controlado", ex);
        return ResponseEntity.internalServerError()
                .body(ApiResponse.error("Ocurrió un error interno. Intente nuevamente más tarde."));
    }

    private String formatearError(FieldError fieldError) {
        return "%s: %s".formatted(fieldError.getField(), fieldError.getDefaultMessage());
    }
}

