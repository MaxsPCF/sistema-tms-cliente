package com.tms.appcliente.operaciones.infrastructure.adapter.out.persistence;

import com.tms.appcliente.operaciones.application.port.out.GeneradorNroSolicitudPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Genera el correlativo NroSolicitud usando una SECUENCIA nativa de SQL Server
 * (atómica y segura bajo concurrencia, sin locks manuales ni condiciones de
 * carrera). Esto NO es un cambio de esquema gestionado por Hibernate
 * (ddl-auto sigue en none): la secuencia es un objeto adicional, propiedad de
 * esta API, que debe crearse una única vez con el siguiente DDL (coordinar
 * con el equipo .NET si van a compartir la numeración):
 *
 * <pre>
 * CREATE SEQUENCE operaciones.SeqNroSolicitud
 *     AS BIGINT
 *     START WITH 1
 *     INCREMENT BY 1
 *     NO CYCLE;
 * </pre>
 */
@Component
public class GeneradorNroSolicitudAdapter implements GeneradorNroSolicitudPort {

    private static final String PREFIJO = "SOL-";

    private final JdbcTemplate jdbcTemplate;

    public GeneradorNroSolicitudAdapter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String generarSiguiente() {
        Long siguiente = jdbcTemplate.queryForObject(
                "SELECT NEXT VALUE FOR operaciones.SeqNroSolicitud", Long.class);
        return PREFIJO + String.format("%06d", siguiente);
    }
}

