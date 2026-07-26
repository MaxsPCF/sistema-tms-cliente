package com.tms.appcliente.seguridad.infrastructure.adapter.out.persistence;

import com.tms.appcliente.seguridad.infrastructure.adapter.out.persistence.entity.RolJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface RolJpaRepository extends JpaRepository<RolJpaEntity, Integer> {

    @Query("""
            select r from RolJpaEntity r
            where r.activo = true
              and r.idRol in (
                  select ur.idRol from UsuarioRolJpaEntity ur
                  where ur.idUsuario = :idUsuario and ur.activo = true
              )
            """)
    List<RolJpaEntity> buscarRolesActivosDeUsuario(@Param("idUsuario") UUID idUsuario);
}

