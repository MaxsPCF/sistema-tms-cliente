package com.tms.appcliente.seguridad.infrastructure.adapter.in.rest.dto;

import java.util.List;

/**
 * DTO de borde HTTP: reproduce exactamente el contrato del requerimiento C
 * (idModulo, idModuloPadre, nombreModulo, icono, ruta, orden, puedeVer,
 * puedeCrear, puedeEditar, puedeEliminar, puedeAprobar, puedeExportar, children).
 */
public record MenuNodoResponse(
        Integer idModulo,
        Integer idModuloPadre,
        String nombreModulo,
        String icono,
        String ruta,
        short orden,
        boolean puedeVer,
        boolean puedeCrear,
        boolean puedeEditar,
        boolean puedeEliminar,
        boolean puedeAprobar,
        boolean puedeExportar,
        List<MenuNodoResponse> children) {
}

