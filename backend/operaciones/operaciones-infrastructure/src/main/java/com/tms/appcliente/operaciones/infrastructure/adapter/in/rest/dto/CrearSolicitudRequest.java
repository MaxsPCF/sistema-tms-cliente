package com.tms.appcliente.operaciones.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * DTO de entrada HTTP. La validación de forma (Bean Validation) vive aquí;
 * las invariantes de negocio (ej. entrega no puede ser antes que carga) viven
 * en el dominio (SolicitudServicio.crear), nunca duplicadas en ambos lados.
 */
public record CrearSolicitudRequest(

        @NotNull(message = "El cliente es obligatorio")
        UUID idCliente,

        Instant fechaHoraCarga,

        Instant fechaHoraEntrega,

        @Size(max = 6, message = "El ubigeo de origen debe tener máximo 6 caracteres")
        String ubigeoOrigen,

        @NotBlank(message = "La dirección de origen es obligatoria")
        @Size(max = 300)
        String origenDireccion,

        @Size(max = 6, message = "El ubigeo de destino debe tener máximo 6 caracteres")
        String ubigeoDestino,

        @NotBlank(message = "La dirección de destino es obligatoria")
        @Size(max = 300)
        String destinoDireccion,

        @Size(max = 100)
        String tipoCarga,

        @PositiveOrZero(message = "El peso de la carga no puede ser negativo")
        BigDecimal pesoCarga,

        @PositiveOrZero(message = "El volumen de la carga no puede ser negativo")
        BigDecimal volumenCarga,

        @Size(max = 10)
        String unidadPeso,

        @Size(max = 500)
        String descripcionCarga,

        @Size(max = 500)
        String observacionCliente
) {
}

