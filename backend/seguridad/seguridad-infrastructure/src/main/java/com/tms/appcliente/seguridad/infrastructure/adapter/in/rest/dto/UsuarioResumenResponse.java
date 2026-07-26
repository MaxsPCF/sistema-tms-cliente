package com.tms.appcliente.seguridad.infrastructure.adapter.in.rest.dto;

import java.util.List;
import java.util.UUID;

public record UsuarioResumenResponse(UUID idUsuario, String nombreUsuario, String email, List<String> roles) {
}

