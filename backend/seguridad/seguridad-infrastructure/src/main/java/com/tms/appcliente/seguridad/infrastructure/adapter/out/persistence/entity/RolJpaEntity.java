package com.tms.appcliente.seguridad.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Rol", schema = "seguridad")
public class RolJpaEntity {

    @Id
    @Column(name = "IdRol")
    private Integer idRol;

    @Column(name = "IdAplicacion", nullable = false)
    private Integer idAplicacion;

    @Column(name = "NombreRol", nullable = false, length = 100)
    private String nombreRol;

    @Column(name = "EsAdmin", nullable = false)
    private Boolean esAdmin;

    @Column(name = "Activo", nullable = false)
    private Boolean activo;

    protected RolJpaEntity() { }

    public Integer getIdRol() { return idRol; }
    public Integer getIdAplicacion() { return idAplicacion; }
    public String getNombreRol() { return nombreRol; }
    public Boolean getEsAdmin() { return esAdmin; }
    public Boolean getActivo() { return activo; }
}

