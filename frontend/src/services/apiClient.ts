import axios, { type AxiosError, type InternalAxiosRequestConfig } from 'axios'
import { ENV } from '../config/env'
import { STORAGE_KEYS } from '../config/constants'
import type { ApiResponse } from '../types/api-response.types'

export const apiClient = axios.create({
  baseURL: ENV.API_BASE_URL,
  timeout: 15000,
  headers: {
    'Content-Type': 'application/json',
  },
})

// Interceptor de request: adjunta el token JWT si existe
apiClient.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const token = localStorage.getItem(STORAGE_KEYS.TOKEN)
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error: AxiosError) => Promise.reject(error)
)

// Interceptor de response: maneja expiración de sesión (401)
apiClient.interceptors.response.use(
  (response) => response,
  (error: AxiosError<ApiResponse<unknown>>) => {
    if (error.response?.status === 401) {
      Object.values(STORAGE_KEYS).forEach((key) => localStorage.removeItem(key))
      if (!window.location.pathname.includes('/login')) {
        window.location.href = '/login'
      }
    }
    return Promise.reject(error)
  }
)

/**
 * Extrae un mensaje de error legible desde una respuesta ApiResponse fallida,
 * o desde un error de red/timeout genérico de Axios.
 */
export function extractApiErrorMessage(error: unknown, fallback = 'Ocurrió un error inesperado'): string {
  if (axios.isAxiosError<ApiResponse<unknown>>(error)) {
    const apiResponse = error.response?.data
    if (apiResponse?.errors?.length) return apiResponse.errors.join(', ')
    if (apiResponse?.message) return apiResponse.message
    if (error.message) return error.message
  }
  return fallback
}

export default apiClient
