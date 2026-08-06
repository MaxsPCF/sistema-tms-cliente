package com.tms.appcliente.seguridad.application.service;

import com.tms.appcliente.seguridad.application.command.AutenticarUsuarioCommand;
import com.tms.appcliente.seguridad.application.port.in.AutenticarUsuarioUseCase;
import com.tms.appcliente.seguridad.application.port.out.PasswordVerifierPort;
import com.tms.appcliente.seguridad.application.port.out.TokenProviderPort;
import com.tms.appcliente.seguridad.application.query.LoginResult;
import com.tms.appcliente.seguridad.application.query.MenuNodo;
import com.tms.appcliente.seguridad.application.query.UsuarioResumen;
import com.tms.appcliente.seguridad.domain.model.Modulo;
import com.tms.appcliente.seguridad.domain.model.Permiso;
import com.tms.appcliente.seguridad.domain.model.Rol;
import com.tms.appcliente.seguridad.domain.model.Usuario;
import com.tms.appcliente.seguridad.domain.port.out.MenuRepository;
import com.tms.appcliente.seguridad.domain.port.out.RolRepository;
import com.tms.appcliente.seguridad.domain.port.out.UsuarioRepository;
import com.tms.appcliente.shared.exception.AccesoNoAutorizadoException;
import com.tms.appcliente.shared.exception.CredencialesInvalidasException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Caso de uso "Iniciar sesión". Orquesta el modelo de dominio (Usuario) y los
 * puertos de salida; NUNCA contiene lógica SQL/JPA/JWT concreta — eso está en
 * infraestructura, inyectado aquí como implementaciones de los puertos.
 *
 * Flujo:
 *  1. Cargar Usuario por nombreUsuario/email (falla genérica si no existe,
 *     para no revelar cuáles identificadores existen -> mitigación de
 *     enumeración de usuarios, OWASP A07).
 *  2. Verificar invariantes de dominio (activo/bloqueado).
 *  3. Verificar contraseña vía puerto (BCrypt Enhanced-compatible).
 *  4. Registrar éxito o fallo (transición de dominio) y persistir.
 *  5. Cargar roles + árbol de menús + permisos, ensamblar el Composite.
 *  6. Emitir JWT.
 */
@Service
public class AutenticarUsuarioService implements AutenticarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final MenuRepository menuRepository;
    private final PasswordVerifierPort passwordVerifier;
    private final TokenProviderPort tokenProvider;

    public AutenticarUsuarioService(UsuarioRepository usuarioRepository,
                                     RolRepository rolRepository,
                                     MenuRepository menuRepository,
                                     PasswordVerifierPort passwordVerifier,
                                     TokenProviderPort tokenProvider) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.menuRepository = menuRepository;
        this.passwordVerifier = passwordVerifier;
        this.tokenProvider = tokenProvider;
    }

    @Override
    @Transactional
    public LoginResult ejecutar(AutenticarUsuarioCommand command) {
        Usuario usuario = usuarioRepository.buscarPorNombreUsuarioOEmail(command.identificador())
                .orElseThrow(() -> new CredencialesInvalidasException("Usuario o contraseña incorrectos."));

        usuario.verificarPuedeIntentarAutenticacion();

        boolean passwordValido = passwordVerifier.verificar(command.password(), usuario.passwordHash());
        if (!passwordValido) {
            usuario.registrarIntentoFallido();
            usuarioRepository.guardar(usuario);
            throw new CredencialesInvalidasException("Usuario o contraseña incorrectos.");
        }

        usuario.registrarAccesoExitoso(Instant.now());
        usuarioRepository.guardar(usuario);

        List<Rol> roles = rolRepository.buscarRolesActivosDeUsuarioPorAplicacion(
                usuario.idUsuario(), command.codigoAplicacion());
        if (roles.isEmpty()) {
            throw new AccesoNoAutorizadoException(
                    "El usuario no tiene acceso habilitado a este canal.");
        }
        List<Integer> idsRoles = roles.stream().map(Rol::idRol).toList();

        List<Modulo> arbolModulos = menuRepository.buscarArbolModulosPorAplicacion(command.codigoAplicacion());
        Map<Integer, Permiso> permisos = menuRepository.buscarPermisosPorRoles(idsRoles, command.codigoAplicacion());
        List<MenuNodo> menus = MenuTreeAssembler.ensamblar(arbolModulos, permisos);

        TokenProviderPort.TokenEmitido tokenEmitido = tokenProvider.generar(usuario, roles);

        UsuarioResumen usuarioResumen = new UsuarioResumen(
                usuario.idUsuario(),
                usuario.nombreUsuario(),
                usuario.email(),
                roles.stream().map(Rol::nombreRol).toList());

        return new LoginResult(tokenEmitido.token(), tokenEmitido.expiracion(), usuarioResumen, menus);
    }
}

