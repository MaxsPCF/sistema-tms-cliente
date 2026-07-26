package com.tms.appcliente.seguridad.domain.model;

/**
 * Value Object de permisos CRUD+ granulares por (Rol, Módulo) — seguridad.Permiso.
 * Se agregan por módulo cuando un usuario tiene más de un rol (OR lógico),
 * ver {@code MenuTreeBuilder} en la capa de aplicación.
 */
public record Permiso(
        Integer idModulo,
        boolean puedeVer,
        boolean puedeCrear,
        boolean puedeEditar,
        boolean puedeEliminar,
        boolean puedeAprobar,
        boolean puedeExportar) {

    public static Permiso vacio(Integer idModulo) {
        return new Permiso(idModulo, false, false, false, false, false, false);
    }

    /** Combina (OR lógico) los permisos de dos roles distintos sobre el mismo módulo. */
    public Permiso combinar(Permiso otro) {
        if (otro == null) return this;
        return new Permiso(
                idModulo,
                puedeVer || otro.puedeVer,
                puedeCrear || otro.puedeCrear,
                puedeEditar || otro.puedeEditar,
                puedeEliminar || otro.puedeEliminar,
                puedeAprobar || otro.puedeAprobar,
                puedeExportar || otro.puedeExportar);
    }
}

