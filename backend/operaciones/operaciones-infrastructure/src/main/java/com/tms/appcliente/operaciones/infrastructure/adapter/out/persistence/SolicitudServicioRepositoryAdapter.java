package com.tms.appcliente.operaciones.infrastructure.adapter.out.persistence;

import com.tms.appcliente.operaciones.domain.model.SolicitudServicio;
import com.tms.appcliente.operaciones.domain.port.out.SolicitudServicioRepository;
import com.tms.appcliente.operaciones.infrastructure.adapter.out.persistence.entity.SolicitudServicioJpaEntity;
import com.tms.appcliente.operaciones.infrastructure.adapter.out.persistence.mapper.SolicitudServicioMapper;
import org.springframework.stereotype.Component;

/** Adaptador de salida (Driven Adapter) del puerto de dominio SolicitudServicioRepository. */
@Component
public class SolicitudServicioRepositoryAdapter implements SolicitudServicioRepository {

    private final SolicitudServicioJpaRepository jpaRepository;

    public SolicitudServicioRepositoryAdapter(SolicitudServicioJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void guardar(SolicitudServicio solicitud) {
        SolicitudServicioJpaEntity entity = SolicitudServicioMapper.aEntity(solicitud);
        jpaRepository.save(entity);
    }
}

