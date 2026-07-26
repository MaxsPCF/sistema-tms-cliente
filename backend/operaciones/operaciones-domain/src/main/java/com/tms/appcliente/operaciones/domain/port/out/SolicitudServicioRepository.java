package com.tms.appcliente.operaciones.domain.port.out;

import com.tms.appcliente.operaciones.domain.model.SolicitudServicio;

public interface SolicitudServicioRepository {

    /** Persiste una nueva solicitud. La tabla ya existe en SQL Server (ddl-auto=none). */
    void guardar(SolicitudServicio solicitud);
}

