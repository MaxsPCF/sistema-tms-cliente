package com.tms.appcliente.operaciones.domain.exception;

/** Violación de una invariante del agregado SolicitudServicio. */
public class SolicitudInvalidaException extends RuntimeException {
    public SolicitudInvalidaException(String message) {
        super(message);
    }
}

