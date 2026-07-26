package com.tms.appcliente.operaciones.domain.vo;

/** Origen o destino de la solicitud: código Ubigeo (FK a maestros.Ubigeo) + dirección textual. */
public record Ubicacion(String codigoUbigeo, String direccion) {
}

