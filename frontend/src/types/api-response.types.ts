/**
 * Réplica del `ApiResponse<T>` del backend (.NET).
 * Todas las respuestas de la API vienen envueltas en esta estructura.
 */
export interface ApiResponse<T> {
	success: boolean;
	data: T | null;
	message: string | null;
	errors: string[];
	timestamp: string;
	traceId: string;
}
