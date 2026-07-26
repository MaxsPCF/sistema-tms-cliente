package com.tms.appcliente.seguridad.infrastructure.adapter.out.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security.jwt")
public class JwtProperties {

    /** Clave secreta (HS384/HS512), inyectada por variable de entorno — nunca hardcodeada. */
    private String secret;
    private long expiracionMinutos = 60;
    private String emisor = "api-cliente-transporte";

    public String getSecret() { return secret; }
    public void setSecret(String secret) { this.secret = secret; }
    public long getExpiracionMinutos() { return expiracionMinutos; }
    public void setExpiracionMinutos(long expiracionMinutos) { this.expiracionMinutos = expiracionMinutos; }
    public String getEmisor() { return emisor; }
    public void setEmisor(String emisor) { this.emisor = emisor; }
}

