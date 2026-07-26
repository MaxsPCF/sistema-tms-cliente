package com.tms.appcliente.bootstrap.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security.rate-limit")
public class RateLimitProperties {

    private int capacidad = 60;
    private int refillPorMinuto = 60;

    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }
    public int getRefillPorMinuto() { return refillPorMinuto; }
    public void setRefillPorMinuto(int refillPorMinuto) { this.refillPorMinuto = refillPorMinuto; }
}

