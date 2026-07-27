package com.tms.appcliente.seguridad.domain.port.out;

import com.tms.appcliente.seguridad.domain.model.Rol;

import java.util.List;
import java.util.UUID;

public interface RolRepository {

    /**
     * Roles activos del usuario, restringidos a los que pertenecen a la
     * aplicación/canal indicado (join UsuarioRol -> Rol -> Aplicacion).
     * Un usuario puede tener roles en varias aplicaciones; este método
     * nunca debe devolver roles fuera del canal solicitado.
     */
    List<Rol> buscarRolesActivosDeUsuarioPorAplicacion(UUID idUsuario, String codigoAplicacion);
}

