package com.tms.appcliente.seguridad.application.port.out;

/**
 * Puerto de salida para verificar contraseñas. La implementación en
 * infraestructura debe ser compatible con
 * {@code BCrypt.Net.BCrypt.EnhancedHashPassword} generado por la aplicación
 * .NET 10 (SHA-384 + BCrypt), ver requerimiento A.
 */
public interface PasswordVerifierPort {
    boolean verificar(String passwordPlano, String passwordHashAlmacenado);
}

