package com.tms.appcliente.operaciones.infrastructure.adapter.out.persistence.mapper;

import com.tms.appcliente.operaciones.domain.model.SolicitudServicio;
import com.tms.appcliente.operaciones.infrastructure.adapter.out.persistence.entity.SolicitudServicioJpaEntity;

/** Traduce el agregado de dominio SolicitudServicio <-> su entidad JPA. */
public final class SolicitudServicioMapper {

    private SolicitudServicioMapper() {
    }

    public static SolicitudServicioJpaEntity aEntity(SolicitudServicio s) {
        return new SolicitudServicioJpaEntity(
                s.idSolicitud(),
                s.nroSolicitud().valor(),
                s.idCliente(),
                s.fechaSolicitud(),
                s.fechaHoraCarga(),
                s.fechaHoraEntrega(),
                s.origen().codigoUbigeo(),
                s.origen().direccion(),
                s.destino().codigoUbigeo(),
                s.destino().direccion(),
                s.carga() == null ? null : s.carga().tipoCarga(),
                s.carga() == null ? null : s.carga().pesoCarga(),
                s.carga() == null ? null : s.carga().volumenCarga(),
                s.carga() == null ? null : s.carga().unidadPeso(),
                s.carga() == null ? null : s.carga().descripcionCarga(),
                s.observacionCliente(),
                s.estado().name(),
                s.idUsuarioCreacion());
    }
}

