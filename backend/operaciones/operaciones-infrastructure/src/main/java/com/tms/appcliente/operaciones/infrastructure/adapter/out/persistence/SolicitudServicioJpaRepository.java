package com.tms.appcliente.operaciones.infrastructure.adapter.out.persistence;

import com.tms.appcliente.operaciones.infrastructure.adapter.out.persistence.entity.SolicitudServicioJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SolicitudServicioJpaRepository extends JpaRepository<SolicitudServicioJpaEntity, UUID> {
}

