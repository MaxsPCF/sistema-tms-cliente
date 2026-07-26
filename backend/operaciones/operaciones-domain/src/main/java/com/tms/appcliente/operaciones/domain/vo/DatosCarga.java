package com.tms.appcliente.operaciones.domain.vo;

import com.tms.appcliente.operaciones.domain.exception.SolicitudInvalidaException;

import java.math.BigDecimal;

/** Encapsula tipoCarga/pesoCarga/volumenCarga/unidadPeso/descripcionCarga con sus invariantes. */
public record DatosCarga(
        String tipoCarga,
        BigDecimal pesoCarga,
        BigDecimal volumenCarga,
        String unidadPeso,
        String descripcionCarga) {

    public DatosCarga {
        if (pesoCarga != null && pesoCarga.signum() < 0) {
            throw new SolicitudInvalidaException("El peso de la carga no puede ser negativo.");
        }
        if (volumenCarga != null && volumenCarga.signum() < 0) {
            throw new SolicitudInvalidaException("El volumen de la carga no puede ser negativo.");
        }
        if (unidadPeso == null || unidadPeso.isBlank()) {
            unidadPeso = "TN"; // valor por defecto de la columna en el esquema
        }
    }
}

