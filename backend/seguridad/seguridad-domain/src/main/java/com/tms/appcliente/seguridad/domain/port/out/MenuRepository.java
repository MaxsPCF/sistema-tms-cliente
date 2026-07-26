package com.tms.appcliente.seguridad.domain.port.out;

import com.tms.appcliente.seguridad.domain.model.Modulo;
import com.tms.appcliente.seguridad.domain.model.Permiso;

import java.util.List;
import java.util.Map;

/**
 * Puerto de salida para construir el árbol de menús (seguridad.Modulo) y los
 * permisos consolidados (seguridad.Permiso) de un conjunto de roles. Se separa
 * en dos consultas porque el árbol de módulos es independiente del rol
 * (catálogo de la aplicación ADMIN_WEB / PORTAL_CLIENTE / APP_CONDUCTOR),
 * mientras que los permisos sí dependen de los roles del usuario autenticado.
 */
public interface MenuRepository {

    /** Árbol completo de módulos (ya anidado, raíces primero) de una aplicación/canal. */
    List<Modulo> buscarArbolModulosPorAplicacion(String codigoAplicacion);

    /** Mapa idModulo -> Permiso, para los roles indicados, en esa aplicación. */
    Map<Integer, Permiso> buscarPermisosPorRoles(List<Integer> idsRoles, String codigoAplicacion);
}

