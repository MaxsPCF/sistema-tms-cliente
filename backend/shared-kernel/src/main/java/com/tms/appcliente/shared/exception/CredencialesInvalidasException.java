package com.tms.appcliente.shared.exception;

import org.springframework.http.HttpStatus;

public class CredencialesInvalidasException extends ApplicationException {
    public CredencialesInvalidasException(String message) {
        super("CREDENCIALES_INVALIDAS", message, HttpStatus.UNAUTHORIZED);
    }
}

