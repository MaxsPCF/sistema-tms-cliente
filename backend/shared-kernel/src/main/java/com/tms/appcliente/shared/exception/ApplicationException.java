package com.tms.appcliente.shared.exception;

import org.springframework.http.HttpStatus;

/**
 * Raíz de la jerarquía de excepciones de negocio. Cada subclase fija su propio
 * HttpStatus, permitiendo que el {@code GlobalExceptionHandler} traduzca
 * automáticamente cualquier excepción de dominio/aplicación al envoltorio
 * estándar de error sin necesidad de un catch por cada caso de uso.
 */
public abstract class ApplicationException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String codigo;

    protected ApplicationException(String codigo, String message, HttpStatus httpStatus) {
        super(message);
        this.codigo = codigo;
        this.httpStatus = httpStatus;
    }

    public HttpStatus httpStatus() {
        return httpStatus;
    }

    public String codigo() {
        return codigo;
    }
}

