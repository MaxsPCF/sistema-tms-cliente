package com.tms.appcliente.seguridad.infrastructure.adapter.in.rest;

import com.tms.appcliente.seguridad.application.query.LoginResult;
import com.tms.appcliente.seguridad.application.query.MenuNodo;
import com.tms.appcliente.seguridad.application.query.UsuarioResumen;
import com.tms.appcliente.seguridad.infrastructure.adapter.in.rest.dto.LoginResponse;
import com.tms.appcliente.seguridad.infrastructure.adapter.in.rest.dto.MenuNodoResponse;
import com.tms.appcliente.seguridad.infrastructure.adapter.in.rest.dto.UsuarioResumenResponse;

import java.util.List;

/** Traduce el read model de aplicación (LoginResult) al DTO exacto de borde HTTP. */
final class LoginResponseMapper {

    private LoginResponseMapper() {
    }

    static LoginResponse aResponse(LoginResult resultado) {
        return new LoginResponse(
                resultado.token(),
                resultado.expiracion(),
                aUsuarioResponse(resultado.usuario()),
                aMenusResponse(resultado.menus()));
    }

    private static UsuarioResumenResponse aUsuarioResponse(UsuarioResumen u) {
        return new UsuarioResumenResponse(u.idUsuario(), u.nombreUsuario(), u.email(), u.roles());
    }

    private static List<MenuNodoResponse> aMenusResponse(List<MenuNodo> nodos) {
        return nodos.stream().map(LoginResponseMapper::aMenuResponse).toList();
    }

    private static MenuNodoResponse aMenuResponse(MenuNodo n) {
        return new MenuNodoResponse(
                n.idModulo(),
                n.idModuloPadre(),
                n.nombreModulo(),
                n.icono(),
                n.ruta(),
                n.orden(),
                n.puedeVer(),
                n.puedeCrear(),
                n.puedeEditar(),
                n.puedeEliminar(),
                n.puedeAprobar(),
                n.puedeExportar(),
                aMenusResponse(n.children())
        );
    }
}

