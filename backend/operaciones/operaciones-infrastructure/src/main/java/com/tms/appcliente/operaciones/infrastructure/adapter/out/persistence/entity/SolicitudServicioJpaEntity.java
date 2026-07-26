package com.tms.appcliente.operaciones.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Mapeo 1:1 de operaciones.SolicitudServicio. ddl-auto=none: la tabla ya
 * existe (compartida con la app .NET 10); esta entidad solo lee/escribe filas,
 * nunca migra esquema.
 */
@Entity
@Table(name = "SolicitudServicio", schema = "operaciones")
public class SolicitudServicioJpaEntity {

    @Id
    @Column(name = "IdSolicitud", columnDefinition = "uniqueidentifier")
    private UUID idSolicitud;

    @Column(name = "NroSolicitud", nullable = false, length = 20, unique = true)
    private String nroSolicitud;

    @Column(name = "IdCliente", nullable = false, columnDefinition = "uniqueidentifier")
    private UUID idCliente;

    @Column(name = "FechaSolicitud", nullable = false)
    private LocalDate fechaSolicitud;

    @Column(name = "FechaHoraCarga")
    private Instant fechaHoraCarga;

    @Column(name = "FechaHoraEntrega")
    private Instant fechaHoraEntrega;

    @Column(name = "UbigeoOrigen", length = 6)
    private String ubigeoOrigen;

    @Column(name = "OrigenDireccion", length = 300)
    private String origenDireccion;

    @Column(name = "UbigeoDestino", length = 6)
    private String ubigeoDestino;

    @Column(name = "DestinoDireccion", length = 300)
    private String destinoDireccion;

    @Column(name = "TipoCarga", length = 100)
    private String tipoCarga;

    @Column(name = "PesoCarga", precision = 10, scale = 2)
    private BigDecimal pesoCarga;

    @Column(name = "VolumenCarga", precision = 10, scale = 2)
    private BigDecimal volumenCarga;

    @Column(name = "UnidadPeso", length = 10)
    private String unidadPeso;

    @Column(name = "DescripcionCarga", length = 500)
    private String descripcionCarga;

    @Column(name = "ObservacionCliente", length = 500)
    private String observacionCliente;

    @Column(name = "Estado", nullable = false, length = 30)
    private String estado;

    @Column(name = "FechaCreacion", nullable = false, updatable = false)
    private Instant fechaCreacion;

    @Column(name = "UsuarioCreacion", nullable = false, columnDefinition = "uniqueidentifier")
    private UUID usuarioCreacion;

    @Column(name = "FechaModificacion")
    private Instant fechaModificacion;

    @Column(name = "UsuarioModifica", columnDefinition = "uniqueidentifier")
    private UUID usuarioModifica;

    protected SolicitudServicioJpaEntity() {
        // JPA
    }

    public SolicitudServicioJpaEntity(UUID idSolicitud, String nroSolicitud, UUID idCliente,
                                       LocalDate fechaSolicitud, Instant fechaHoraCarga, Instant fechaHoraEntrega,
                                       String ubigeoOrigen, String origenDireccion, String ubigeoDestino,
                                       String destinoDireccion, String tipoCarga, BigDecimal pesoCarga,
                                       BigDecimal volumenCarga, String unidadPeso, String descripcionCarga,
                                       String observacionCliente, String estado, UUID usuarioCreacion) {
        this.idSolicitud = idSolicitud;
        this.nroSolicitud = nroSolicitud;
        this.idCliente = idCliente;
        this.fechaSolicitud = fechaSolicitud;
        this.fechaHoraCarga = fechaHoraCarga;
        this.fechaHoraEntrega = fechaHoraEntrega;
        this.ubigeoOrigen = ubigeoOrigen;
        this.origenDireccion = origenDireccion;
        this.ubigeoDestino = ubigeoDestino;
        this.destinoDireccion = destinoDireccion;
        this.tipoCarga = tipoCarga;
        this.pesoCarga = pesoCarga;
        this.volumenCarga = volumenCarga;
        this.unidadPeso = unidadPeso;
        this.descripcionCarga = descripcionCarga;
        this.observacionCliente = observacionCliente;
        this.estado = estado;
        this.usuarioCreacion = usuarioCreacion;
    }

    public UUID getIdSolicitud() { return idSolicitud; }
    public String getNroSolicitud() { return nroSolicitud; }
    public UUID getIdCliente() { return idCliente; }
    public LocalDate getFechaSolicitud() { return fechaSolicitud; }
    public Instant getFechaHoraCarga() { return fechaHoraCarga; }
    public Instant getFechaHoraEntrega() { return fechaHoraEntrega; }
    public String getUbigeoOrigen() { return ubigeoOrigen; }
    public String getOrigenDireccion() { return origenDireccion; }
    public String getUbigeoDestino() { return ubigeoDestino; }
    public String getDestinoDireccion() { return destinoDireccion; }
    public String getTipoCarga() { return tipoCarga; }
    public BigDecimal getPesoCarga() { return pesoCarga; }
    public BigDecimal getVolumenCarga() { return volumenCarga; }
    public String getUnidadPeso() { return unidadPeso; }
    public String getDescripcionCarga() { return descripcionCarga; }
    public String getObservacionCliente() { return observacionCliente; }
    public String getEstado() { return estado; }
    public UUID getUsuarioCreacion() { return usuarioCreacion; }
}

