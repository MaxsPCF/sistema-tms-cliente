package com.tms.appcliente.seguridad.infrastructure.adapter.in.rest;

import com.tms.appcliente.seguridad.application.command.AutenticarUsuarioCommand;
import com.tms.appcliente.seguridad.application.port.in.AutenticarUsuarioUseCase;
import com.tms.appcliente.seguridad.application.query.LoginResult;
import com.tms.appcliente.seguridad.infrastructure.adapter.in.rest.dto.LoginRequest;
import com.tms.appcliente.seguridad.infrastructure.adapter.in.rest.dto.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import com.tms.appcliente.shared.web.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Adaptador de entrada REST del módulo de Seguridad. Solo traduce HTTP <->
 * Command/Query; toda la orquestación real vive en {@link AutenticarUsuarioUseCase}.
 * La respuesta se devuelve "cruda" (LoginResponse); {@code ApiResponseAdvice}
 * (shared-kernel) la envuelve automáticamente en el sobre estándar
 * {success, data, message, errors} — así se cumple el requerimiento B sin
 * ensuciar este controlador con ese detalle transversal.
 */
@RestController
@RequestMapping("/api/v1/authenticate")
@Tag(name = "Autenticación", description = "Login, emisión de JWT y menús/permisos del usuario")
public class AuthenticateController {

    private final AutenticarUsuarioUseCase autenticarUsuarioUseCase;
    private static final String CODIGO_APLICACION = "PORTAL_CLIENTE";

    public AuthenticateController(AutenticarUsuarioUseCase autenticarUsuarioUseCase) {
        this.autenticarUsuarioUseCase = autenticarUsuarioUseCase;
    }

    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica al usuario y devuelve el JWT junto con el árbol de menús y "
                    + "permisos (RBAC) consolidados para el canal indicado.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login exitoso")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Cuenta bloqueada o inactiva")
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        AutenticarUsuarioCommand command = new AutenticarUsuarioCommand(
                request.usuario(), request.password(), CODIGO_APLICACION);
        LoginResult resultado = autenticarUsuarioUseCase.ejecutar(command);
        LoginResponse response = LoginResponseMapper.aResponse(resultado);
        return ApiResponse.ok(response, "Login exitoso.");
    }
}

