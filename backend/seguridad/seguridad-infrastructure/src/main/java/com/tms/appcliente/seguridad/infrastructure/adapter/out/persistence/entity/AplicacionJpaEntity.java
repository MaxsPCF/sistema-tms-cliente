package com.tms.appcliente.seguridad.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Aplicacion", schema = "seguridad")
public class AplicacionJpaEntity {

    @Id
    @Column(name = "IdAplicacion")
    private Integer idAplicacion;

    @Column(name = "Codigo", nullable = false, length = 30)
    private String codigo;

    @Column(name = "Activo", nullable = false)
    private Boolean activo;

    protected AplicacionJpaEntity() { }

    public Integer getIdAplicacion() { return idAplicacion; }
    public String getCodigo() { return codigo; }
    public Boolean getActivo() { return activo; }
}

