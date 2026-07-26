package com.tms.appcliente.operaciones.application.command;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Comando CQRS (write-side) para "Crear Solicitud". Es un DTO de aplicación
 * plano (sin anotaciones de validación HTTP; eso vive en el DTO del adaptador
 * REST) que ya trae resuelto el idUsuarioSolicitante desde el JWT.
 */
public record CrearSolicitudCommand(
        UUID idCliente,
        Instant fechaHoraCarga,
        Instant fechaHoraEntrega,
        String ubigeoOrigen,
        String origenDireccion,
        String ubigeoDestino,
        String destinoDireccion,
        String tipoCarga,
        BigDecimal pesoCarga,
        BigDecimal volumenCarga,
        String unidadPeso,
        String descripcionCarga,
        String observacionCliente,
        UUID idUsuarioSolicitante) {
}

