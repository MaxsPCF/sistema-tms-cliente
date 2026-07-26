package com.tms.appcliente.seguridad.infrastructure.adapter.out.persistence;

import com.tms.appcliente.seguridad.infrastructure.adapter.out.persistence.entity.UsuarioJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioJpaRepository extends JpaRepository<UsuarioJpaEntity, UUID> {

    // Nombre de usuario o email: la app .NET también los usa como únicos.
    Optional<UsuarioJpaEntity> findByNombreUsuarioIgnoreCaseOrEmailIgnoreCase(String nombreUsuario, String email);
}

