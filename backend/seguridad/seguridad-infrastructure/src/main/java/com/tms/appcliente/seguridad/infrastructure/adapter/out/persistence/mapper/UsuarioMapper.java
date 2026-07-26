package com.tms.appcliente.seguridad.infrastructure.adapter.out.persistence.mapper;

import com.tms.appcliente.seguridad.domain.model.Usuario;
import com.tms.appcliente.seguridad.infrastructure.adapter.out.persistence.entity.UsuarioJpaEntity;

/** Traduce entre el modelo de dominio puro Usuario y su entidad JPA. */
public final class UsuarioMapper {

    private UsuarioMapper() {
    }

    public static Usuario aDominio(UsuarioJpaEntity e) {
        return new Usuario(
                e.getIdUsuario(),
                e.getIdPersona(),
                e.getNombreUsuario(),
                e.getEmail(),
                e.getPasswordHash(),
                e.getUltimoAcceso(),
                e.getIntentosFallidos() == null ? 0 : e.getIntentosFallidos(),
                Boolean.TRUE.equals(e.getBloqueado()),
                Boolean.TRUE.equals(e.getActivo()));
    }

    /** Copia el estado mutable del dominio de vuelta a la entidad administrada por JPA. */
    public static void volcarEstadoATransaccional(Usuario usuario, UsuarioJpaEntity entity) {
        entity.setUltimoAcceso(usuario.ultimoAcceso());
        entity.setIntentosFallidos((short) usuario.intentosFallidos());
        entity.setBloqueado(usuario.bloqueado());
        entity.setFechaModificacion(java.time.Instant.now());
    }
}

