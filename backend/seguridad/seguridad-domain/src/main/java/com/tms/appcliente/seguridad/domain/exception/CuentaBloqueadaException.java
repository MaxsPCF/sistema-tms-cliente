package com.tms.appcliente.seguridad.domain.exception;

/** Regla de dominio: un Usuario bloqueado o inactivo no puede autenticarse. */
public class CuentaBloqueadaException extends RuntimeException {
    public CuentaBloqueadaException(String message) {
        super(message);
    }
}

