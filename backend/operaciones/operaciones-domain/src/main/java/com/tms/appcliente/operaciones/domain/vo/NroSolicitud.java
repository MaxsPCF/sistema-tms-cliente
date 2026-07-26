package com.tms.appcliente.operaciones.domain.vo;

import com.tms.appcliente.operaciones.domain.exception.SolicitudInvalidaException;

/** NVARCHAR(20) UNIQUE — se genera en infraestructura (correlativo) y se valida aquí su forma. */
public record NroSolicitud(String valor) {

    public NroSolicitud {
        if (valor == null || valor.isBlank()) {
            throw new SolicitudInvalidaException("El número de solicitud no puede estar vacío.");
        }
        if (valor.length() > 20) {
            throw new SolicitudInvalidaException("El número de solicitud excede la longitud permitida (20).");
        }
    }
}

