package com.tms.appcliente.seguridad.application.query;

import java.util.List;
import java.util.UUID;

public record UsuarioResumen(UUID idUsuario, String nombreUsuario, String email, List<String> roles) {
}

