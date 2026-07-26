package com.tms.appcliente.seguridad.domain.model;

import java.util.List;

/**
 * Nodo de menú (seguridad.Modulo) modelado como estructura Composite: cada
 * módulo puede tener módulos hijos (IdModuloPadre autorreferenciado), lo que
 * permite construir el árbol de hasta N niveles requerido por el login
 * (requerimiento C exige al menos 3 niveles).
 */
public record Modulo(
        Integer idModulo,
        Integer idModuloPadre,
        String nombreModulo,
        String icono,
        String ruta,
        short orden,
        List<Modulo> children) {

    public Modulo {
        children = children == null ? List.of() : List.copyOf(children);
    }

    public boolean esRaiz() {
        return idModuloPadre == null;
    }
}

