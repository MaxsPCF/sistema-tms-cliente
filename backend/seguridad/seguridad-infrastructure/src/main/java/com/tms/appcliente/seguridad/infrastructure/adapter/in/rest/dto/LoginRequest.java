package com.tms.appcliente.seguridad.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "El usuario o email es obligatorio")
        String usuario,
        @NotBlank(message = "La contraseña es obligatoria")
        String password
) {}

