export const ENV = {
	API_BASE_URL: import.meta.env.VITE_API_BASE_URL || "http://localhost:7137/api/v1",
	USE_MOCK_API: import.meta.env.VITE_USE_MOCK_API === "false",
	APP_NAME: import.meta.env.VITE_APP_NAME || "TMS-Cliente - Transporte Carga Pesada",
} as const;
