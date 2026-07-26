package com.tms.appcliente.seguridad.infrastructure.adapter.out.security;

import com.tms.appcliente.seguridad.application.port.out.PasswordVerifierPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Adaptador de salida (requerimiento A): verifica contraseñas contra hashes
 * generados por la app .NET 10 con:
 *
 * <pre>BCrypt.Net.BCrypt.EnhancedHashPassword(plainPassword, workFactor: 12)</pre>
 *
 * La variante "Enhanced" de BCrypt.Net-Next NO aplica bcrypt directamente
 * sobre la contraseña: primero la pre-hashea con SHA-384 (evitando el límite
 * de 72 bytes de bcrypt y añadiendo una capa adicional) y codifica ese
 * digest en Base64; ese texto Base64 es el que efectivamente entra al
 * algoritmo bcrypt estándar. El hash bcrypt resultante ($2a$/$2b$...) es
 * 100% estándar, por lo que basta con reproducir el mismo pre-hash en Java
 * y delegar la comparación final a una implementación bcrypt estándar
 * (aquí, la de spring-security-crypto).
 *
 * IMPORTANTE: antes de ir a producción, validar este adaptador contra un
 * lote real de hashes generados por la app .NET (test de integración
 * cruzada), ya que el formato exacto de "Enhanced" no está versionado
 * públicamente como un estándar y puede variar entre versiones de la
 * librería BCrypt.Net-Next.
 */
@Component
public class EnhancedBCryptPasswordVerifier implements PasswordVerifierPort {

    private static final Logger log = LoggerFactory.getLogger(EnhancedBCryptPasswordVerifier.class);
    private static final String ALGORITMO_PREHASH = "SHA-384";

    @Override
    public boolean verificar(String passwordPlano, String passwordHashAlmacenado) {
        if (passwordPlano == null || passwordHashAlmacenado == null) {
            return false;
        }
        try {
            String preHasheado = preHashSha384Base64(passwordPlano);
            return BCrypt.checkpw(preHasheado, passwordHashAlmacenado);
        } catch (IllegalArgumentException ex) {
            // Hash almacenado con formato bcrypt inválido/corrupto: se trata como
            // credencial no verificable, nunca se propaga el detalle al cliente.
            log.warn("Formato de hash bcrypt inválido al verificar credenciales.");
            return false;
        }
    }

    private String preHashSha384Base64(String passwordPlano) {
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITMO_PREHASH);
            byte[] hash = digest.digest(passwordPlano.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            // SHA-384 es un algoritmo estándar del JDK; no debería ocurrir nunca.
            throw new IllegalStateException("Algoritmo de pre-hash no disponible en esta JVM.", e);
        }
    }
}

