package com.tms.appcliente.seguridad.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "Permiso", schema = "seguridad")
public class PermisoJpaEntity {

    @Id
    @Column(name = "IdPermiso", columnDefinition = "uniqueidentifier")
    private UUID idPermiso;

    @Column(name = "IdRol", nullable = false)
    private Integer idRol;

    @Column(name = "IdModulo", nullable = false)
    private Integer idModulo;

    @Column(name = "PuedeVer", nullable = false)
    private Boolean puedeVer;
    @Column(name = "PuedeCrear", nullable = false)
    private Boolean puedeCrear;
    @Column(name = "PuedeEditar", nullable = false)
    private Boolean puedeEditar;
    @Column(name = "PuedeEliminar", nullable = false)
    private Boolean puedeEliminar;
    @Column(name = "PuedeAprobar", nullable = false)
    private Boolean puedeAprobar;
    @Column(name = "PuedeExportar", nullable = false)
    private Boolean puedeExportar;

    @Column(name = "Activo", nullable = false)
    private Boolean activo;

    protected PermisoJpaEntity() { }

    public Integer getIdRol() { return idRol; }
    public Integer getIdModulo() { return idModulo; }
    public Boolean getPuedeVer() { return puedeVer; }
    public Boolean getPuedeCrear() { return puedeCrear; }
    public Boolean getPuedeEditar() { return puedeEditar; }
    public Boolean getPuedeEliminar() { return puedeEliminar; }
    public Boolean getPuedeAprobar() { return puedeAprobar; }
    public Boolean getPuedeExportar() { return puedeExportar; }
    public Boolean getActivo() { return activo; }
}

