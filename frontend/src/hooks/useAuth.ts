import { useCallback } from 'react'
import { useAppDispatch, useAppSelector } from './useAppRedux'
import { loginThunk, logout as logoutAction, clearError } from '../context/authSlice'
import type { LoginRequestDto } from '../types/auth.types'

export function useAuth() {
  const dispatch = useAppDispatch()
  const { usuario, token, menus, isAuthenticated, status, error } = useAppSelector((state) => state.auth)

  const login = useCallback((credentials: LoginRequestDto) => dispatch(loginThunk(credentials)), [dispatch])
  const logout = useCallback(() => dispatch(logoutAction()), [dispatch])
  const resetError = useCallback(() => dispatch(clearError()), [dispatch])

  return {
    usuario,
    token,
    menus,
    isAuthenticated,
    isLoading: status === 'loading',
    error,
    login,
    logout,
    resetError,
  }
}
