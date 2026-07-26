package com.tms.appcliente.operaciones.infrastructure.adapter.in.rest.dto;

import java.time.LocalDate;
import java.util.UUID;

public record CrearSolicitudResponse(UUID idSolicitud, String nroSolicitud, String estado, LocalDate fechaSolicitud) {
}

