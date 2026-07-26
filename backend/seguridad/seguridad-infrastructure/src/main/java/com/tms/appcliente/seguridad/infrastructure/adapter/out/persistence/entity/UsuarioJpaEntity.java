package com.tms.appcliente.seguridad.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

/**
 * Mapeo 1:1 de seguridad.Usuario. ddl-auto=none: esta entidad NUNCA crea ni
 * migra esquema; la tabla ya existe y es compartida con la app .NET 10.
 */
@Entity
@Table(name = "Usuario", schema = "seguridad")
public class UsuarioJpaEntity {

    @Id
    @Column(name = "IdUsuario", columnDefinition = "uniqueidentifier")
    private UUID idUsuario;

    @Column(name = "IdPersona", columnDefinition = "uniqueidentifier")
    private UUID idPersona;

    @Column(name = "NombreUsuario", nullable = false, length = 200)
    private String nombreUsuario;

    @Column(name = "Email", nullable = false, length = 150)
    private String email;

    @Column(name = "PasswordHash", nullable = false, length = 500)
    private String passwordHash;

    @Column(name = "UltimoAcceso")
    private Instant ultimoAcceso;

    @Column(name = "IntentosFallidos", nullable = false)
    private Short intentosFallidos;

    @Column(name = "Bloqueado", nullable = false)
    private Boolean bloqueado;

    @Column(name = "Activo", nullable = false)
    private Boolean activo;

    @Column(name = "FechaCreacion", nullable = false, updatable = false)
    private Instant fechaCreacion;

    @Column(name = "UsuarioCreacion", columnDefinition = "uniqueidentifier")
    private UUID usuarioCreacion;

    @Column(name = "FechaModificacion")
    private Instant fechaModificacion;

    @Column(name = "UsuarioModifica", columnDefinition = "uniqueidentifier")
    private UUID usuarioModifica;

    protected UsuarioJpaEntity() {
        // JPA
    }

    public UsuarioJpaEntity(UUID idUsuario, UUID idPersona, String nombreUsuario, String email,
                             String passwordHash, Instant ultimoAcceso, Short intentosFallidos,
                             Boolean bloqueado, Boolean activo) {
        this.idUsuario = idUsuario;
        this.idPersona = idPersona;
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.passwordHash = passwordHash;
        this.ultimoAcceso = ultimoAcceso;
        this.intentosFallidos = intentosFallidos;
        this.bloqueado = bloqueado;
        this.activo = activo;
    }

    public UUID getIdUsuario() { return idUsuario; }
    public UUID getIdPersona() { return idPersona; }
    public String getNombreUsuario() { return nombreUsuario; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public Instant getUltimoAcceso() { return ultimoAcceso; }
    public Short getIntentosFallidos() { return intentosFallidos; }
    public Boolean getBloqueado() { return bloqueado; }
    public Boolean getActivo() { return activo; }
    public Instant getFechaModificacion() { return fechaModificacion; }

    public void setUltimoAcceso(Instant ultimoAcceso) { this.ultimoAcceso = ultimoAcceso; }
    public void setIntentosFallidos(Short intentosFallidos) { this.intentosFallidos = intentosFallidos; }
    public void setBloqueado(Boolean bloqueado) { this.bloqueado = bloqueado; }
    public void setFechaModificacion(Instant fechaModificacion) { this.fechaModificacion = fechaModificacion; }
}

