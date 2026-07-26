package com.tms.appcliente.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Composition root de la API. Este es el ÚNICO módulo que conoce la
 * existencia física de todos los adaptadores concretos (seguridad-*,
 * operaciones-*); el resto de módulos solo se conocen a través de puertos.
 *
 * El componentScan se ancla en el package raíz "com.tms.appcliente" para
 * levantar los @Component/@Service/@RestController repartidos en los jars de
 * dominio/aplicación/infraestructura de ambos módulos de negocio.
 */
@SpringBootApplication(scanBasePackages = "com.tms.appcliente")
@ConfigurationPropertiesScan("com.tms.appcliente")
@EnableJpaRepositories(basePackages = "com.tms.appcliente")
@EntityScan(basePackages = "com.tms.appcliente")
public class TMSClienteApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TMSClienteApiApplication.class, args);
    }
}

