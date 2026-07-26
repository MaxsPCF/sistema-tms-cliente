package com.tms.appcliente.seguridad.application.query;

import java.util.List;

/**
 * Read model (lado Query de CQRS) que representa un nodo del árbol de menús
 * ya decorado con los permisos consolidados del usuario autenticado. Esta es
 * la forma exacta que exige el requerimiento C (estructura Composite anidada).
 */
public record MenuNodo(
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
        List<MenuNodo> children) {
}

