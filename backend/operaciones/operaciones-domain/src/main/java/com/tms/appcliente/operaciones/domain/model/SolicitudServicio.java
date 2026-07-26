package com.tms.appcliente.operaciones.domain.model;

import com.tms.appcliente.operaciones.domain.exception.SolicitudInvalidaException;
import com.tms.appcliente.operaciones.domain.vo.DatosCarga;
import com.tms.appcliente.operaciones.domain.vo.EstadoSolicitud;
import com.tms.appcliente.operaciones.domain.vo.NroSolicitud;
import com.tms.appcliente.operaciones.domain.vo.Ubicacion;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Aggregate Root de operaciones.SolicitudServicio. Modelo de dominio puro:
 * ninguna anotación JPA/Jackson/Bean Validation. La única forma de crear una
 * solicitud válida es a través del factory {@link #crear}, que impone las
 * invariantes del negocio (requerimiento 3: vertical "Crear Solicitud").
 */
public final class SolicitudServicio {

    private final UUID idSolicitud;
    private final NroSolicitud nroSolicitud;
    private final UUID idCliente;
    private final LocalDate fechaSolicitud;
    private final Instant fechaHoraCarga;
    private final Instant fechaHoraEntrega;
    private final Ubicacion origen;
    private final Ubicacion destino;
    private final DatosCarga carga;
    private final String observacionCliente;
    private final EstadoSolicitud estado;
    private final UUID idUsuarioCreacion;

    private SolicitudServicio(UUID idSolicitud, NroSolicitud nroSolicitud, UUID idCliente,
                               LocalDate fechaSolicitud, Instant fechaHoraCarga, Instant fechaHoraEntrega,
                               Ubicacion origen, Ubicacion destino, DatosCarga carga,
                               String observacionCliente, EstadoSolicitud estado, UUID idUsuarioCreacion) {
        this.idSolicitud = idSolicitud;
        this.nroSolicitud = nroSolicitud;
        this.idCliente = idCliente;
        this.fechaSolicitud = fechaSolicitud;
        this.fechaHoraCarga = fechaHoraCarga;
        this.fechaHoraEntrega = fechaHoraEntrega;
        this.origen = origen;
        this.destino = destino;
        this.carga = carga;
        this.observacionCliente = observacionCliente;
        this.estado = estado;
        this.idUsuarioCreacion = idUsuarioCreacion;
    }

    /**
     * Factory de creación (comando "Crear Solicitud"). Siempre nace en estado
     * PENDIENTE (default del esquema) y con un IdSolicitud nuevo, generado en
     * el dominio (no en la base de datos) para poder referenciarlo en la
     * misma transacción de aplicación antes del INSERT.
     */
    public static SolicitudServicio crear(NroSolicitud nroSolicitud, UUID idCliente, Instant fechaHoraCarga,
                                           Instant fechaHoraEntrega, Ubicacion origen, Ubicacion destino,
                                           DatosCarga carga, String observacionCliente, UUID idUsuarioCreacion) {
        if (idCliente == null) {
            throw new SolicitudInvalidaException("El cliente es obligatorio.");
        }
        if (idUsuarioCreacion == null) {
            throw new SolicitudInvalidaException("No se pudo determinar el usuario que crea la solicitud.");
        }
        if (fechaHoraCarga != null && fechaHoraEntrega != null && fechaHoraEntrega.isBefore(fechaHoraCarga)) {
            throw new SolicitudInvalidaException(
                    "La fecha/hora de entrega no puede ser anterior a la fecha/hora de carga.");
        }
        if (origen == null || destino == null) {
            throw new SolicitudInvalidaException("El origen y el destino son obligatorios.");
        }

        return new SolicitudServicio(
                UUID.randomUUID(),
                nroSolicitud,
                idCliente,
                LocalDate.now(),
                fechaHoraCarga,
                fechaHoraEntrega,
                origen,
                destino,
                carga,
                observacionCliente,
                EstadoSolicitud.PENDIENTE,
                idUsuarioCreacion);
    }

    /** Reconstrucción desde persistencia (usada por el mapper de infraestructura). */
    public static SolicitudServicio reconstruir(UUID idSolicitud, NroSolicitud nroSolicitud, UUID idCliente,
                                                 LocalDate fechaSolicitud, Instant fechaHoraCarga,
                                                 Instant fechaHoraEntrega, Ubicacion origen, Ubicacion destino,
                                                 DatosCarga carga, String observacionCliente,
                                                 EstadoSolicitud estado, UUID idUsuarioCreacion) {
        return new SolicitudServicio(idSolicitud, nroSolicitud, idCliente, fechaSolicitud, fechaHoraCarga,
                fechaHoraEntrega, origen, destino, carga, observacionCliente, estado, idUsuarioCreacion);
    }

    public UUID idSolicitud() { return idSolicitud; }
    public NroSolicitud nroSolicitud() { return nroSolicitud; }
    public UUID idCliente() { return idCliente; }
    public LocalDate fechaSolicitud() { return fechaSolicitud; }
    public Instant fechaHoraCarga() { return fechaHoraCarga; }
    public Instant fechaHoraEntrega() { return fechaHoraEntrega; }
    public Ubicacion origen() { return origen; }
    public Ubicacion destino() { return destino; }
    public DatosCarga carga() { return carga; }
    public String observacionCliente() { return observacionCliente; }
    public EstadoSolicitud estado() { return estado; }
    public UUID idUsuarioCreacion() { return idUsuarioCreacion; }

    @Override
    public boolean equals(Object o) {
        return o instanceof SolicitudServicio s && s.idSolicitud.equals(this.idSolicitud);
    }

    @Override
    public int hashCode() {
        return idSolicitud.hashCode();
    }
}

