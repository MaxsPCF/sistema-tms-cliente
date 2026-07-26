package com.tms.appcliente.bootstrap.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    @Value("${server.port:7137}")
    private int serverPort;

    private static final String BEARER_SCHEME = "Bearer Authentication";

    @Bean
    public OpenAPI transporteCargaOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Transporte Carga Pesada - Cliente :: API")
                        .description("""
                                API REST de Seguridad (JWT/RBAC) y Operaciones.
                                ## 🏗️ Arquitectura
                                - **Hexagonal** (Ports & Adapters) con separación limpia de capas
                                - **DDD** (Domain-Driven Design) con Bounded Contexts
                                - **CQRS** (Command Query Responsibility Segregation)
                                - **Event-Driven** con Eventos de Dominio
                                
                                ## 📦 Módulos
                                - **Seguridad**: Autenticación JWT, RBAC, Menús dinámicos tipo Composite
                                - **Operaciones**: Solicitudes, Cotizaciones, Órdenes de servicio y de viaje
                                
                                ## ✨ Características
                                - ✅ Compatible con .NET (BCrypt Enhanced Hashing SHA-384)
                                - ✅ Rate Limiting y protección OWASP Top 10
                                - ✅ Respuesta universal estandarizada
                                - ✅ Paginación y ordenamiento unificados
                                
                                ## 🔐 Seguridad
                                - JWT con refresh tokens
                                - RBAC con permisos granulares (Ver, Crear, Editar, Eliminar, Aprobar, Exportar)
                                - Bloqueo de cuenta por intentos fallidos
                                - Políticas de contraseñas configurables
                                """
                        )
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("TMS-Transporte Dev Team").email("dev@tms-cliente-transporte.com")
                                .url("https://tms-cliente-transporte.com"))
                        .license(new License()
                                .name("Propietario - Todos los derechos reservados")
                                .url("https://tms.com/license")))
                .servers(List.of(
                        new Server().url("http://localhost:" + serverPort)
                                .description("Desarrollo Local"),
                        new Server().url("https://dev-api.tms-cliente-transporte.com")
                                .description("Desarrollo"),
                        new Server().url("https://staging-api.tms-cliente-transporte.com")
                                .description("Staging"),
                        new Server().url("https://api.tms-cliente-transporte.com")
                                .description("Producción")))
                .tags(List.of(
                        new Tag().name("Autenticación")
                                .description("Login, JWT, gestión de sesiones y tokens"),
                        new Tag().name("Solicitudes")
                                .description("Gestión de solicitudes"),
                        new Tag().name("Cotizaciones")
                                .description("Gestión de cotizaciones"),
                        new Tag().name("Ordenes de servicios")
                                .description("Gestión de ordenes de servicio"),
                        new Tag().name("Órdenes de Viaje")
                                .description("Gestión de órdenes de viaje")))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_SCHEME))
                .components(new Components()
                        .addSecuritySchemes(BEARER_SCHEME, createSecurityScheme()));
    }

    private SecurityScheme createSecurityScheme() {
        return new SecurityScheme()
                .name(BEARER_SCHEME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("""
                        Ingrese el token JWT obtenido en el endpoint de login.
                        Formato: `Bearer {token}`
                        El token se obtiene en `POST /api/v1/authenticate/login`
                        **Roles disponibles:**
                        - Cliente (Portal Externo)
                        """);
    }
}

