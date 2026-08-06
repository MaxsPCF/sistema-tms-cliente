# Backend

---

# TMS-Cliente - Transporte Carga Pesada :: API REST

Arquitectura de referencia: **Hexagonal (Ports & Adapters) + DDD + CQRS**, Java 25,
Spring Boot 4.0.7, SQL Server 2025 (esquema compartido con una app .NET 10, `ddl-auto=none`).

## 1. Estructura de módulos

```
api-cliente-transporte/                  (parent pom, packaging=pom)
├── shared-kernel/                     ApiResponse<T>, ApiResponseAdvice, GlobalExceptionHandler
├── seguridad/
│   ├── seguridad-domain/               Usuario (aggregate), Rol, Modulo (Composite), Permiso — sin Spring
│   ├── seguridad-application/          AutenticarUsuarioUseCase (CQRS command), MenuTreeAssembler
│   └── seguridad-infrastructure/       AuthController, JPA (seguridad.*), JWT, BCrypt-Enhanced
├── operaciones/
│   ├── operaciones-domain/             SolicitudServicio (aggregate) — sin Spring
│   ├── operaciones-application/        CrearSolicitudUseCase (CQRS command)
│   └── operaciones-infrastructure/     SolicitudController, JPA (operaciones.SolicitudServicio)
└── bootstrap-api/                       @SpringBootApplication, SecurityFilterChain, application.yml
```

**Regla de dependencia (hexagonal):** `domain` no depende de nada; `application` depende de
`domain` (+ shared-kernel para excepciones/`@Transactional`); `infrastructure` depende de
`application`. Solo `bootstrap-api` conoce todos los adaptadores concretos a la vez.

Los módulos `*-domain` son intencionalmente jars sin ninguna dependencia de framework: eso
es lo que permite testear las invariantes de negocio (`Usuario.registrarIntentoFallido()`,
`SolicitudServicio.crear()`) con JUnit puro, sin levantar Spring ni una base de datos.

## 2. Vertical de referencia: "Crear Solicitud"

```
SolicitudController (REST, valida forma + @PreAuthorize RBAC)
   -> CrearSolicitudCommand (DTO de aplicación, ya con idUsuarioSolicitante resuelto del JWT)
      -> CrearSolicitudUseCase / CrearSolicitudService (orquesta, @Transactional)
         -> SolicitudServicio.crear(...) (dominio puro: valida invariantes de negocio)
            -> SolicitudServicioRepository (puerto de salida)
               -> SolicitudServicioRepositoryAdapter -> JPA -> operaciones.SolicitudServicio
```

El número de solicitud (`NroSolicitud`, `UNIQUE`) se genera con una **SECUENCIA nativa de
SQL Server** (`operaciones.SeqNroSolicitud`), no con Hibernate: es atómica bajo concurrencia
y no viola `ddl-auto=none` (es un objeto adicional, no una migración de tablas existentes).
El DDL para crearla una única vez está documentado en `GeneradorNroSolicitudAdapter`.

## 3. Compatibilidad de contraseñas .NET ⇄ Java (requerimiento A)

`EnhancedBCryptPasswordVerifier` reproduce `BCrypt.Net.BCrypt.EnhancedHashPassword`:
SHA-384 sobre la contraseña en texto plano → Base64 del digest → ese texto entra a un
bcrypt estándar (workFactor 12), verificado en Java con `spring-security-crypto`.

⚠️ **Antes de producción**: validar este adaptador con un lote real de hashes generados
por la app .NET (test de integración cruzada). El formato "Enhanced" de BCrypt.Net-Next no
es un estándar público versionado y puede diferir sutilmente entre versiones de la librería.

## 4. Estrategia de seguridad (Spring Security + JWT + OWASP Top 10)

### 4.1 Autenticación y sesión

- **Stateless JWT** (`SessionCreationPolicy.STATELESS`, sin `JSESSIONID`): elimina fijación
  y robo de sesión. El token se firma con HS384/512 (`JwtTokenProvider`, clave en
  `JWT_SECRET`, **nunca hardcodeada ni versionada** — mitigación A02, Fallas Criptográficas).
- **CSRF deshabilitado deliberadamente**: solo es relevante para autenticación basada en
  cookies enviadas automáticamente por el navegador. Con `Authorization: Bearer <token>`
  no hay nada que un sitio malicioso pueda "arrastrar" sin que el atacante ya posea el token.
- **`JwtAuthenticationFilter`** puebla el `SecurityContext` en cada request; un token
  inválido/expirado nunca revela el motivo exacto al cliente (evita un oráculo de ataque).

### 4.2 Control de acceso (A01 - Broken Access Control)

- RBAC declarativo con `@PreAuthorize("hasAnyRole(...)")` en el punto de uso (controladores),
  no disperso en `if`s dentro de servicios — auditable y difícil de olvidar.
  `@EnableMethodSecurity` en `SecurityConfig`.
- El árbol de menús/permisos (`seguridad.Permiso`) es la fuente de verdad para lo que el
  **frontend** debe mostrar/ocultar, pero la autorización real siempre se re-valida en el
  backend vía `@PreAuthorize` — nunca se confía en que el cliente respete `puedeVer=false`.
- `JsonAccessDeniedHandler` / `JsonAuthenticationEntryPoint` aseguran que incluso los 401/403
  generados dentro de la cadena de filtros (antes de llegar a un controlador) respeten el
  mismo sobre `{success, data, message, errors}` del requerimiento B.

### 4.3 Inyección SQL (A03)

- **100% JPA/JPQL parametrizado** (`@Query` con `:parámetros` nombrados) en todos los
  repositorios — cero concatenación de strings SQL en ningún adaptador.
- Bean Validation (`jakarta.validation`) en los DTOs de entrada rechaza payloads malformados
  antes de que lleguen a la capa de aplicación.

### 4.4 XSS (A03) y serialización

- La API es **JSON puro** (`Content-Type: application/json`); no se renderiza HTML server-side
  con datos de usuario, que es el vector clásico de XSS reflejado/almacenado.
- Jackson serializa records/DTOs tipados (nunca `Map<String,Object>` genérico ni HTML crudo),
  lo que evita inyección de marcado en las respuestas.
- Responsabilidad compartida con el frontend: SPAs (React/Angular) deben seguir escapando
  al renderizar (esto está fuera del alcance de una API, pero se documenta como límite).

### 4.5 Rate limiting / fuerza bruta (A07)

- `RateLimitingFilter` (Bucket4j, token bucket por IP) se ejecuta **antes** de la
  autenticación, cortando ataques de fuerza bruta/credential stuffing contra
  `/api/v1/auth/login` sin gastar ciclos de verificación de contraseña.
- Además, `Usuario` implementa bloqueo de cuenta a nivel de **dominio** tras 5 intentos
  fallidos (`registrarIntentoFallido()`), una segunda capa independiente del rate limit de IP.
- Limitación documentada: el bucket en memoria no es válido para múltiples instancias;
  requiere backend distribuido (`bucket4j-redis`) en un despliegue horizontal real.

### 4.6 Configuración segura (A05)

- CORS restrictivo por **whitelist explícita** de orígenes (nunca `*` junto con credenciales).
- Cabeceras de seguridad explícitas: `X-Frame-Options: DENY`, `X-Content-Type-Options`,
  `Strict-Transport-Security` (HSTS).
- Actuator expone únicamente `health` e `info`; nunca `/env`, `/heapdump` sin protección.
- `GlobalExceptionHandler` nunca propaga `stack traces` ni mensajes internos al cliente en
  errores 500 (evita fuga de detalles de implementación).

### 4.7 Auditoría

- Todas las tablas del esquema ya incluyen `FechaCreacion/UsuarioCreacion` y
  `FechaModificacion/UsuarioModifica`; los adaptadores de persistencia los completan
  siempre a partir del usuario resuelto del JWT (`Authentication.getName()`), nunca de un
  campo enviado libremente por el cliente en el body (evitando que un usuario falsifique
  "quién" hizo una acción).

## 5. Cómo ejecutar

```bash
export DB_URL="jdbc:sqlserver://localhost:1433;databaseName=TransporteCarga;encrypt=true"
export DB_USERNAME=...
export DB_PASSWORD=...
export JWT_SECRET=$(openssl rand -base64 64)   # mínimo 48 bytes para HS384

mvn -pl bootstrap-api -am spring-boot:run
```

Swagger UI: `http://localhost:7137/swagger-ui.html`

## 6. Pendiente antes de producción (fuera del alcance de este esqueleto)

- Validación cruzada real del `EnhancedBCryptPasswordVerifier` contra hashes .NET.
- Ejecutar el DDL de `operaciones.SeqNroSolicitud` (ver comentario en `GeneradorNroSolicitudAdapter`).
- Sustituir el backend de rate limiting por uno distribuido si hay más de una instancia.
- Tests: unitarios de dominio (sin Spring), de integración con Testcontainers (SQL Server),
  y contractuales del endpoint de login contra el JSON exacto del requerimiento C.

---

---

# Frontend

---

# TMS-Cliente - Transporte Carga Pesada :: (TypeScript) — Login + Layout

Migración a **TypeScript** del portal de clientes, construida **módulo por módulo** contra el contrato real del
backend (.NET + MediatR). Esta primera entrega cubre: **Login** y **Layout** (Header + Sidebar dinámico + Content).

---

## ⚠️ Supuestos que debes validar contra tu backend

1. **Casing JSON = camelCase.** Tu backend serializa DTOs en PascalCase en C# (`Email`, `IdUsuario`, `PuedeVer`...),
   pero por defecto `System.Text.Json` en ASP.NET Core convierte a **camelCase** en el JSON de salida
   (`email`, `idUsuario`, `puedeVer`...), y el model binding de entrada es case-insensitive. Todos los tipos en
   `src/types/auth.types.ts` asumen camelCase. **Si tu backend tiene `PropertyNamingPolicy = null`** (mantiene
   PascalCase), avísame y ajusto los tipos y el `apiClient`.
2. **Endpoint real**: `POST {VITE_API_BASE_URL}/Authenticate/login`, deducido de
   `[Route("api/v1/[controller]")]` sobre `AuthenticateController`.
3. **Expiración de sesión**: uso el campo `expiracion` (fecha ISO) para invalidar la sesión guardada en
   `localStorage` sin esperar un 401 del servidor. Si el backend maneja refresh token, aún no está implementado
   (dime si lo necesitas y lo agrego).

Mientras no conectes el backend real, deja `VITE_USE_MOCK_API=true`: el `authService` simula la **misma forma exacta**
de respuesta (`ApiResponse<LoginResponseDto>`) que tu backend, incluyendo un árbol de menús de ejemplo con 6 módulos
(Inicio, Solicitudes, Cotizaciones, Órdenes, Viajes y un grupo "Administración > Usuarios") para que puedas validar
el Sidebar dinámico y sus permisos.

## 🗂️ Contrato tipado (`src/types/`)

- `api-response.types.ts` → réplica exacta de `ApiResponse<T>`.
- `auth.types.ts` → réplica exacta de `LoginRequestDto`, `LoginResponseDto`, `UsuarioInfoDto`,
  `AccesoPermisoModuloDto` (con su árbol recursivo `children`).

Estos tipos son la **fuente de verdad** del contrato backend↔frontend. Cuando compartas los DTOs de Solicitud,
Cotización, Orden, Viaje y Usuario, seguiremos el mismo patrón: un archivo `types/<modulo>.types.ts` que refleja el
DTO 1:1.

## 🧩 Sidebar dinámico dirigido por permisos

El menú **ya no es una constante estática**: se construye en tiempo real a partir de `LoginResponseDto.Menus`, que
llega en el login y se guarda en Redux (`auth.menus`) + `localStorage`.

- `layouts/Sidebar.tsx` recorre `menus` (ordenados por `orden`) y renderiza `NavMenuItem` por cada uno.
- `layouts/NavMenuItem.tsx` es **recursivo**: si un módulo tiene `children`, se muestra como grupo desplegable
  (ej. "Administración"); si no tiene `ruta`, actúa solo como agrupador (no navega).
- Un módulo con `puedeVer: false` **no se renderiza** (los permisos de creación/edición/eliminación/aprobación/
  exportación se usarán más adelante para mostrar/ocultar botones dentro de cada módulo — ver `usePermission`).
- `utils/resolveIcon.ts` mapea el string `icono` del backend (ej. `"Truck"`) a un componente de Lucide, usando un
  **mapa curado** (no el catálogo completo) para no romper el tree-shaking. Si el backend agrega un ícono nuevo que
  no está en el mapa, cae a un ícono de respaldo (`CircleDot`) y basta con importarlo y añadirlo a `ICON_MAP`.

## 🔐 Flujo de autenticación

- `services/authService.ts` → `login()` llama a `POST /Authenticate/login`, desenvuelve `ApiResponse<T>`, valida
  `success`, y persiste `token`, `expiracion`, `usuario` y `menus` en `localStorage`.
- `context/authSlice.ts` (Redux Toolkit) → `loginThunk` tipado con `createAsyncThunk<LoginResponseDto, LoginRequestDto>`.
- `hooks/useAuth.ts` → hook de conveniencia (usuario, menús, login, logout, estado de carga/error).
- `hooks/usePermission.ts` → dado un `ruta`, busca el módulo en el árbol de menús y devuelve sus 6 flags de permiso;
  listo para usarse en los próximos módulos (ej. ocultar el botón "Nueva solicitud" si `puedeCrear` es `false`).
- `routes/ProtectedRoute.tsx` / `PublicRoute.tsx` → guardas de ruta según `isAuthenticated`.

## 📁 Estructura

```
src/
├── types/               # Contratos 1:1 con los DTOs del backend
├── services/            # apiClient.ts (axios + interceptores JWT), authService.ts
├── context/              # store.ts, authSlice.ts, uiSlice.ts (Redux Toolkit)
├── hooks/                 # useAuth, usePermission, useAppRedux (hooks tipados)
├── layouts/                # MainLayout, AuthLayout, Header, Sidebar, NavMenuItem (recursivo)
├── routes/                  # AppRouter, ProtectedRoute, PublicRoute
├── pages/
│   ├── auth/                 # LoginPage (funcional) · RegisterPage (placeholder, pendiente de contrato)
│   └── dashboard/               # DashboardPage (muestra el árbol de menús/permisos recibido, temporal)
├── components/atoms/              # Button, Input, Card, Badge, Spinner
└── utils/                           # cn.ts, formatters.ts, resolveIcon.ts
```

## ⚙️ Instalación

```bash
npm install
cp .env.example .env
npm run dev      # http://localhost:5173
npm run build    # type-check (tsc -b) + build de producción
```

Login demo (modo mock): cualquier correo válido + contraseña de 6+ caracteres.

## 🔜 Próximos pasos (módulo por módulo)

1. ~~Login + Layout~~ ✅ (esta entrega)
2. Módulo **Usuarios**
3. Módulo **Solicitudes**
4. Módulo **Cotizaciones**
5. Módulo **Órdenes**
6. Módulo **Viajes**

Para cada módulo necesito los DTOs del backend (request/response) igual que compartiste para login, así los tipos
quedan exactos y no hay que adivinar formas de datos.
