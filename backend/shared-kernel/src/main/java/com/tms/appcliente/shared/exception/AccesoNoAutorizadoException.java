package com.tms.appcliente.shared.exception;

import org.springframework.http.HttpStatus;

public class AccesoNoAutorizadoException extends ApplicationException {
    public AccesoNoAutorizadoException(String message) {
        super("ACCESO_NO_AUTORIZADO", message, HttpStatus.FORBIDDEN);
    }
}

