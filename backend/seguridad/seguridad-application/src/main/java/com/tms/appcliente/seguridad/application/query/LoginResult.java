package com.tms.appcliente.seguridad.application.query;

import java.time.OffsetDateTime;
import java.util.List;

public record LoginResult(
        String token,
        OffsetDateTime expiracion,
        UsuarioResumen usuario,
        List<MenuNodo> menus) {
}

