package com.tms.appcliente.seguridad.infrastructure.adapter.out.persistence;

import com.tms.appcliente.seguridad.infrastructure.adapter.out.persistence.entity.PermisoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PermisoJpaRepository extends JpaRepository<PermisoJpaEntity, java.util.UUID> {

    @Query("""
            select p from PermisoJpaEntity p
            join ModuloJpaEntity m on m.idModulo = p.idModulo
            join AplicacionJpaEntity a on a.idAplicacion = m.idAplicacion
            where p.idRol in :idsRoles and a.codigo = :codigoAplicacion and p.activo = true
            """)
    List<PermisoJpaEntity> buscarPorRolesYAplicacion(@Param("idsRoles") List<Integer> idsRoles,
                                                       @Param("codigoAplicacion") String codigoAplicacion);
}

