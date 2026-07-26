package com.tms.appcliente.seguridad.domain.model;

import com.tms.appcliente.seguridad.domain.exception.CuentaBloqueadaException;

import java.time.Instant;
import java.util.UUID;

/**
 * Aggregate Root de seguridad.Usuario. Encapsula las invariantes de acceso:
 * bloqueo por intentos fallidos, habilitación de cuenta y registro de último
 * acceso. Es un modelo de dominio puro: no conoce Spring Security, JPA ni JWT;
 * esos son detalles de infraestructura inyectados a través de puertos.
 */
public final class Usuario {

    private static final int MAX_INTENTOS_FALLIDOS = 5;

    private final UUID idUsuario;
    private final UUID idPersona;
    private final String nombreUsuario;
    private final String email;
    private final String passwordHash;
    private Instant ultimoAcceso;
    private int intentosFallidos;
    private boolean bloqueado;
    private final boolean activo;

    public Usuario(UUID idUsuario, UUID idPersona, String nombreUsuario, String email,
                    String passwordHash, Instant ultimoAcceso, int intentosFallidos,
                    boolean bloqueado, boolean activo) {
        this.idUsuario = idUsuario;
        this.idPersona = idPersona;
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.passwordHash = passwordHash;
        this.ultimoAcceso = ultimoAcceso;
        this.intentosFallidos = intentosFallidos;
        this.bloqueado = bloqueado;
        this.activo = activo;
    }

    /** Verifica precondiciones de dominio ANTES de intentar validar la contraseña. */
    public void verificarPuedeIntentarAutenticacion() {
        if (!activo) {
            throw new CuentaBloqueadaException("La cuenta de usuario se encuentra inactiva.");
        }
        if (bloqueado) {
            throw new CuentaBloqueadaException(
                    "La cuenta se encuentra bloqueada por exceso de intentos fallidos. Contacte al administrador.");
        }
    }

    /** Transición de dominio tras una autenticación exitosa. */
    public void registrarAccesoExitoso(Instant momento) {
        this.intentosFallidos = 0;
        this.ultimoAcceso = momento;
    }

    /** Transición de dominio tras una contraseña incorrecta; bloquea al superar el umbral. */
    public void registrarIntentoFallido() {
        this.intentosFallidos++;
        if (this.intentosFallidos >= MAX_INTENTOS_FALLIDOS) {
            this.bloqueado = true;
        }
    }

    public UUID idUsuario() { return idUsuario; }
    public UUID idPersona() { return idPersona; }
    public String nombreUsuario() { return nombreUsuario; }
    public String email() { return email; }
    public String passwordHash() { return passwordHash; }
    public Instant ultimoAcceso() { return ultimoAcceso; }
    public int intentosFallidos() { return intentosFallidos; }
    public boolean bloqueado() { return bloqueado; }
    public boolean activo() { return activo; }

    @Override
    public boolean equals(Object o) {
        return o instanceof Usuario u && u.idUsuario.equals(this.idUsuario);
    }

    @Override
    public int hashCode() {
        return idUsuario.hashCode();
    }
}

