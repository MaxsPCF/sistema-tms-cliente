package com.tms.appcliente.seguridad.application.port.out;

import com.tms.appcliente.seguridad.domain.model.Rol;
import com.tms.appcliente.seguridad.domain.model.Usuario;

import java.time.OffsetDateTime;
import java.util.List;

public interface TokenProviderPort {

    TokenEmitido generar(Usuario usuario, List<Rol> roles);

    record TokenEmitido(String token, OffsetDateTime expiracion) {}
}

