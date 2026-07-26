package com.tms.appcliente.seguridad.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "UsuarioRol", schema = "seguridad")
public class UsuarioRolJpaEntity {

    @Id
    @Column(name = "IdUsuarioRol", columnDefinition = "uniqueidentifier")
    private UUID idUsuarioRol;

    @Column(name = "IdUsuario", nullable = false, columnDefinition = "uniqueidentifier")
    private UUID idUsuario;

    @Column(name = "IdRol", nullable = false)
    private Integer idRol;

    @Column(name = "Activo", nullable = false)
    private Boolean activo;

    protected UsuarioRolJpaEntity() { }

    public UUID getIdUsuario() { return idUsuario; }
    public Integer getIdRol() { return idRol; }
    public Boolean getActivo() { return activo; }
}

