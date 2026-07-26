package com.tms.appcliente.operaciones.application.command;

import java.time.LocalDate;
import java.util.UUID;

public record CrearSolicitudResult(
        UUID idSolicitud,
        String nroSolicitud,
        String estado,
        LocalDate fechaSolicitud) {
}

