package com.tms.appcliente.operaciones.infrastructure.adapter.in.rest;

import com.tms.appcliente.operaciones.application.command.CrearSolicitudCommand;
import com.tms.appcliente.operaciones.application.command.CrearSolicitudResult;
import com.tms.appcliente.operaciones.application.port.in.CrearSolicitudUseCase;
import com.tms.appcliente.operaciones.infrastructure.adapter.in.rest.dto.CrearSolicitudRequest;
import com.tms.appcliente.operaciones.infrastructure.adapter.in.rest.dto.CrearSolicitudResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Adaptador de entrada REST del vertical "Crear Solicitud" (requerimiento 3).
 * Responsabilidades EXCLUSIVAS de este controlador: (1) traducir HTTP -> Command,
 * (2) resolver el usuario autenticado desde el JWT, (3) traducir el resultado
 * de aplicación -> DTO de respuesta. Ninguna regla de negocio vive aquí.
 */
@RestController
@RequestMapping("/api/v1/solicitudes")
@Tag(name = "Solicitudes de Servicio", description = "Gestión de solicitudes de transporte de carga")
@SecurityRequirement(name = "bearerAuth")
public class SolicitudController {

    private final CrearSolicitudUseCase crearSolicitudUseCase;

    public SolicitudController(CrearSolicitudUseCase crearSolicitudUseCase) {
        this.crearSolicitudUseCase = crearSolicitudUseCase;
    }

    @Operation(summary = "Crear una nueva solicitud de servicio de transporte")
    @ApiResponse(responseCode = "201", description = "Solicitud creada")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    @ApiResponse(responseCode = "422", description = "Violación de una regla de negocio")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    // RBAC declarativo: solo roles con permiso de creación en el módulo Solicitudes.
    @PreAuthorize("hasAnyRole('CLIENTE', 'COORDINADOR_OPERACIONES', 'ADMIN')")
    public CrearSolicitudResponse crear(@Valid @RequestBody CrearSolicitudRequest request,
                                         Authentication authentication) {
        UUID idUsuarioSolicitante = UUID.fromString(authentication.getName());

        CrearSolicitudCommand command = new CrearSolicitudCommand(
                request.idCliente(),
                request.fechaHoraCarga(),
                request.fechaHoraEntrega(),
                request.ubigeoOrigen(),
                request.origenDireccion(),
                request.ubigeoDestino(),
                request.destinoDireccion(),
                request.tipoCarga(),
                request.pesoCarga(),
                request.volumenCarga(),
                request.unidadPeso(),
                request.descripcionCarga(),
                request.observacionCliente(),
                idUsuarioSolicitante);

        CrearSolicitudResult resultado = crearSolicitudUseCase.ejecutar(command);

        return new CrearSolicitudResponse(
                resultado.idSolicitud(), resultado.nroSolicitud(), resultado.estado(), resultado.fechaSolicitud());
    }
}

