package com.tms.appcliente.shared.exception;

import org.springframework.http.HttpStatus;

/** Violación de una invariante o regla de negocio del dominio (422). */
public class ReglaDeNegocioException extends ApplicationException {
    public ReglaDeNegocioException(String message) {
        super("REGLA_DE_NEGOCIO", message, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}

