package com.tms.appcliente.seguridad.infrastructure.adapter.in.rest.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record LoginResponse(
        String token,
        OffsetDateTime expiracion,
        UsuarioResumenResponse usuario,
        List<MenuNodoResponse> menus) {
}

