package com.tms.appcliente.seguridad.infrastructure.adapter.out.persistence;

import com.tms.appcliente.seguridad.infrastructure.adapter.out.persistence.entity.ModuloJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ModuloJpaRepository extends JpaRepository<ModuloJpaEntity, Integer> {

    @Query("""
            select m from ModuloJpaEntity m
            join AplicacionJpaEntity a on a.idAplicacion = m.idAplicacion
            where a.codigo = :codigoAplicacion and m.activo = true
            order by m.orden asc
            """)
    List<ModuloJpaEntity> buscarPorAplicacion(@Param("codigoAplicacion") String codigoAplicacion);
}

