package com.tms.appcliente.seguridad.domain.model;

/**
 * Rol de seguridad (seguridad.Rol). Value/entity de catálogo referenciado por
 * Usuario a través de la asociación UsuarioRol.
 */
public record Rol(Integer idRol, Integer idAplicacion, String nombreRol, boolean esAdmin, boolean activo) {
}

