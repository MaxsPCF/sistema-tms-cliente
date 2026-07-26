package com.tms.appcliente.seguridad.application.service;

import com.tms.appcliente.seguridad.application.query.MenuNodo;
import com.tms.appcliente.seguridad.domain.model.Modulo;
import com.tms.appcliente.seguridad.domain.model.Permiso;

import java.util.List;
import java.util.Map;

/**
 * Decora el árbol de módulos (catálogo, independiente del usuario) con los
 * permisos consolidados de los roles del usuario autenticado, produciendo el
 * read model {@link MenuNodo} anidado exigido por el requerimiento C.
 * Es una función pura: mismo árbol + mismo mapa de permisos -> mismo resultado.
 */
public final class MenuTreeAssembler {

    private MenuTreeAssembler() {
    }

    public static List<MenuNodo> ensamblar(List<Modulo> arbol, Map<Integer, Permiso> permisosPorModulo) {
        return arbol.stream().map(modulo -> aNodo(modulo, permisosPorModulo)).toList();
    }

    private static MenuNodo aNodo(Modulo modulo, Map<Integer, Permiso> permisosPorModulo) {
        Permiso permiso = permisosPorModulo.getOrDefault(modulo.idModulo(), Permiso.vacio(modulo.idModulo()));
        List<MenuNodo> hijos = modulo.children().stream()
                .map(hijo -> aNodo(hijo, permisosPorModulo))
                .toList();
        return new MenuNodo(
                modulo.idModulo(),
                modulo.idModuloPadre(),
                modulo.nombreModulo(),
                modulo.icono(),
                modulo.ruta(),
                modulo.orden(),
                permiso.puedeVer(),
                permiso.puedeCrear(),
                permiso.puedeEditar(),
                permiso.puedeEliminar(),
                permiso.puedeAprobar(),
                permiso.puedeExportar(),
                hijos);
    }
}

