package com.tms.appcliente.seguridad.application.port.in;

import com.tms.appcliente.seguridad.application.command.AutenticarUsuarioCommand;
import com.tms.appcliente.seguridad.application.query.LoginResult;

/**
 * Puerto de entrada (Driving Port). El adaptador REST (Controller) solo
 * conoce esta interfaz; nunca invoca directamente al servicio de aplicación
 * ni a un repositorio.
 */
public interface AutenticarUsuarioUseCase {
    LoginResult ejecutar(AutenticarUsuarioCommand command);
}

