import apiClient, { extractApiErrorMessage } from "./apiClient";
import { STORAGE_KEYS } from "../config/constants";
import type { ApiResponse } from "../types/api-response.types";
import type { LoginRequestDto, LoginResponseDto } from "../types/auth.types";

/**
 * Inicia sesión contra POST /Authenticate/login.
 * En modo mock, simula la misma forma de respuesta (ApiResponse<LoginResponseDto>).
 */
async function login(credentials: LoginRequestDto): Promise<LoginResponseDto> {
	try {
		const { data: response } = await apiClient.post<ApiResponse<LoginResponseDto>>("/authenticate/login", credentials);
		if (!response.success || !response.data) {
			throw new Error(response.message || "No se pudo iniciar sesión");
		}
		persistSession(response.data);
		return response.data;
	} catch (error) {
		throw new Error(extractApiErrorMessage(error, "No se pudo iniciar sesión"));
	}
}

function logout(): void {
	// agregar servico logout backend
	Object.values(STORAGE_KEYS).forEach(key => localStorage.removeItem(key));
}

function persistSession(data: LoginResponseDto): void {
	localStorage.setItem(STORAGE_KEYS.TOKEN, data.token);
	localStorage.setItem(STORAGE_KEYS.EXPIRACION, data.expiracion);
	localStorage.setItem(STORAGE_KEYS.USUARIO, JSON.stringify(data.usuario));
	localStorage.setItem(STORAGE_KEYS.MENUS, JSON.stringify(data.menus));
}

function getStoredSession(): LoginResponseDto | null {
	const token = localStorage.getItem(STORAGE_KEYS.TOKEN);
	const expiracion = localStorage.getItem(STORAGE_KEYS.EXPIRACION);
	const usuarioRaw = localStorage.getItem(STORAGE_KEYS.USUARIO);
	const menusRaw = localStorage.getItem(STORAGE_KEYS.MENUS);

	if (!token || !expiracion || !usuarioRaw || !menusRaw) return null;

	// Sesión expirada localmente: se limpia y se fuerza nuevo login.
	if (new Date(expiracion).getTime() <= Date.now()) {
		logout();
		return null;
	}

	return {
		token,
		expiracion,
		usuario: JSON.parse(usuarioRaw),
		menus: JSON.parse(menusRaw),
	};
}

export const authService = { login, logout, getStoredSession };
