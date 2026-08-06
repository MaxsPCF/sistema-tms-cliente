/**
 * Réplica de `LoginRequestDto` (backend).
 * record LoginRequestDto(string Email, string Password)
 */
export interface LoginRequestDto {
	usuario: string;
	password: string;
}

/**
 * Réplica de `UsuarioInfoDto` (backend).
 * record UsuarioInfoDto(Guid IdUsuario, string NombreUsuario, string Email, IEnumerable<string> Roles)
 */
export interface UsuarioInfoDto {
	idUsuario: string;
	nombreUsuario: string;
	email: string;
	roles: string[];
}

/**
 * Réplica de `AccesoPermisoModuloDto` (backend).
 * Nodo del árbol de menú, con sus permisos (CRUD + Aprobar + Exportar) y submódulos (Children).
 */
export interface AccesoPermisoModuloDto {
	idModulo: number;
	idModuloPadre: number | null;
	nombreModulo: string;
	icono: string | null;
	ruta: string | null;
	orden: number;
	puedeVer: boolean;
	puedeCrear: boolean;
	puedeEditar: boolean;
	puedeEliminar: boolean;
	puedeAprobar: boolean;
	puedeExportar: boolean;
	children: AccesoPermisoModuloDto[];
}

/**
 * Réplica de `LoginResponseDto` (backend).
 * record LoginResponseDto(string Token, DateTime Expiracion, UsuarioInfoDto Usuario, IEnumerable<AccesoPermisoModuloDto> Menus)
 */
export interface LoginResponseDto {
	token: string;
	expiracion: string; // fecha ISO serializada por System.Text.Json
	usuario: UsuarioInfoDto;
	menus: AccesoPermisoModuloDto[];
}

/**
 * Acciones de permiso soportadas por un módulo. Se usan para validar
 * accesos en la UI (botones, rutas, acciones de tabla, etc.).
 */
export type PermisoAccion = "puedeVer" | "puedeCrear" | "puedeEditar" | "puedeEliminar" | "puedeAprobar" | "puedeExportar";
