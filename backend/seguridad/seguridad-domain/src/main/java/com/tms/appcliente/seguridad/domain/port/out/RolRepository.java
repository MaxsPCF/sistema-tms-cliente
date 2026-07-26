package com.tms.appcliente.seguridad.domain.port.out;

import com.tms.appcliente.seguridad.domain.model.Rol;

import java.util.List;
import java.util.UUID;

public interface RolRepository {

    /** Roles activos asignados vigentes de un usuario (join UsuarioRol). */
    List<Rol> buscarRolesActivosDeUsuario(UUID idUsuario);
}

