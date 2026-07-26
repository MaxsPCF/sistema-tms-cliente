package com.tms.appcliente.seguridad.domain.port.out;

import com.tms.appcliente.seguridad.domain.model.Usuario;

import java.util.Optional;
import java.util.UUID;

/**
 * Puerto de salida (Driven Port) para la persistencia de Usuario. La
 * implementación (adaptador JPA/SQL Server) vive en seguridad-infrastructure;
 * el dominio y la aplicación solo conocen esta interfaz.
 */
public interface UsuarioRepository {

    Optional<Usuario> buscarPorNombreUsuarioOEmail(String nombreUsuarioOEmail);

    Optional<Usuario> buscarPorId(UUID idUsuario);

    void guardar(Usuario usuario);
}

