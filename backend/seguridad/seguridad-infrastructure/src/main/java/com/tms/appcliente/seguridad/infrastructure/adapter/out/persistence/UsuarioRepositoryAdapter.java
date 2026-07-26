package com.tms.appcliente.seguridad.infrastructure.adapter.out.persistence;

import com.tms.appcliente.seguridad.domain.model.Usuario;
import com.tms.appcliente.seguridad.domain.port.out.UsuarioRepository;
import com.tms.appcliente.seguridad.infrastructure.adapter.out.persistence.entity.UsuarioJpaEntity;
import com.tms.appcliente.seguridad.infrastructure.adapter.out.persistence.mapper.UsuarioMapper;
import com.tms.appcliente.shared.exception.RecursoNoEncontradoException;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Adaptador de salida (Driven Adapter): implementa el puerto de dominio
 * {@link UsuarioRepository} sobre Spring Data JPA sin filtrar detalles JPA
 * hacia el dominio ni la aplicación.
 */
@Component
public class UsuarioRepositoryAdapter implements UsuarioRepository {

    private final UsuarioJpaRepository jpaRepository;

    public UsuarioRepositoryAdapter(UsuarioJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Usuario> buscarPorNombreUsuarioOEmail(String nombreUsuarioOEmail) {
        return jpaRepository
                .findByNombreUsuarioIgnoreCaseOrEmailIgnoreCase(nombreUsuarioOEmail, nombreUsuarioOEmail)
                .map(UsuarioMapper::aDominio);
    }

    @Override
    public Optional<Usuario> buscarPorId(UUID idUsuario) {
        return jpaRepository.findById(idUsuario).map(UsuarioMapper::aDominio);
    }

    @Override
    public void guardar(Usuario usuario) {
        UsuarioJpaEntity entity = jpaRepository.findById(usuario.idUsuario())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Usuario no encontrado al intentar persistir cambios de acceso."));
        UsuarioMapper.volcarEstadoATransaccional(usuario, entity);
        jpaRepository.save(entity);
    }
}

