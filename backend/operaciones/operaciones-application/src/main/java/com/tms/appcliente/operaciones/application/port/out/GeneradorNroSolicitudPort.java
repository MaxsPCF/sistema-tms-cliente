package com.tms.appcliente.operaciones.application.port.out;

/**
 * Genera el correlativo único NroSolicitud (NVARCHAR(20), UQ_SolicitudServicio_Nro).
 * Implementado en infraestructura, típicamente contra una secuencia/tabla de
 * correlativos en SQL Server para garantizar unicidad bajo concurrencia.
 */
public interface GeneradorNroSolicitudPort {
    String generarSiguiente();
}

