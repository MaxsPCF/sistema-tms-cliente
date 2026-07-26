package com.tms.appcliente.seguridad.application.command;

/**
 * Comando CQRS de entrada para el caso de uso de autenticación. "Login" se
 * modela como Command (no Query) porque produce efectos de escritura:
 * actualiza UltimoAcceso/IntentosFallidos del Usuario.
 *
 * @param identificador   NombreUsuario o Email (seguridad.Usuario admite ambos como únicos).
 * @param password        Contraseña en texto plano recibida por HTTPS (nunca se loguea).
 * @param codigoAplicacion Canal de acceso: ADMIN_WEB | PORTAL_CLIENTE | APP_CONDUCTOR.
 */
public record AutenticarUsuarioCommand(String identificador, String password, String codigoAplicacion) {
}

