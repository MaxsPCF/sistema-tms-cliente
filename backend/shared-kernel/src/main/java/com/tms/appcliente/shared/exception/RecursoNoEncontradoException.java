package com.tms.appcliente.shared.exception;

import org.springframework.http.HttpStatus;

public class RecursoNoEncontradoException extends ApplicationException {
    public RecursoNoEncontradoException(String message) {
        super("RECURSO_NO_ENCONTRADO", message, HttpStatus.NOT_FOUND);
    }
}

