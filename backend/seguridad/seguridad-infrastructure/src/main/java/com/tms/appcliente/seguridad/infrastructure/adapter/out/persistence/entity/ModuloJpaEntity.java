package com.tms.appcliente.seguridad.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Modulo", schema = "seguridad")
public class ModuloJpaEntity {

    @Id
    @Column(name = "IdModulo")
    private Integer idModulo;

    @Column(name = "IdAplicacion", nullable = false)
    private Integer idAplicacion;

    @Column(name = "IdModuloPadre")
    private Integer idModuloPadre;

    @Column(name = "NombreModulo", nullable = false, length = 100)
    private String nombreModulo;

    @Column(name = "Icono", length = 100)
    private String icono;

    @Column(name = "Ruta", length = 200)
    private String ruta;

    @Column(name = "Orden", nullable = false)
    private Short orden;

    @Column(name = "Activo", nullable = false)
    private Boolean activo;

    protected ModuloJpaEntity() { }

    public Integer getIdModulo() { return idModulo; }
    public Integer getIdAplicacion() { return idAplicacion; }
    public Integer getIdModuloPadre() { return idModuloPadre; }
    public String getNombreModulo() { return nombreModulo; }
    public String getIcono() { return icono; }
    public String getRuta() { return ruta; }
    public Short getOrden() { return orden; }
    public Boolean getActivo() { return activo; }
}

