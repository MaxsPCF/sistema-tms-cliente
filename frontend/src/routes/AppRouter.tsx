import { lazy, Suspense } from 'react'
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import Spinner from '../components/atoms/Spinner'
import ProtectedRoute from './ProtectedRoute'
import PublicRoute from './PublicRoute'
import AuthLayout from '../layouts/AuthLayout'
import MainLayout from '../layouts/MainLayout'

const LoginPage = lazy(() => import('../pages/auth/LoginPage'))
const RegisterPage = lazy(() => import('../pages/auth/RegisterPage'))
const DashboardPage = lazy(() => import('../pages/dashboard/DashboardPage'))
const NotFoundPage = lazy(() => import('../pages/NotFoundPage'))

function PageFallback() {
  return (
    <div className="flex min-h-screen items-center justify-center">
      <Spinner label="Cargando..." />
    </div>
  )
}

export default function AppRouter() {
  return (
    <BrowserRouter>
      <Suspense fallback={<PageFallback />}>
        <Routes>
          <Route path="/" element={<Navigate to="/dashboard" replace />} />

          <Route element={<PublicRoute />}>
            <Route element={<AuthLayout />}>
              <Route path="/login" element={<LoginPage />} />
              <Route path="/registro" element={<RegisterPage />} />
            </Route>
          </Route>

          <Route element={<ProtectedRoute />}>
            <Route element={<MainLayout />}>
              <Route path="/dashboard" element={<DashboardPage />} />
              {/* Los módulos de Solicitud, Cotización, Orden, Viaje y Usuario
                  se incorporarán aquí en las siguientes iteraciones. */}
            </Route>
          </Route>

          <Route path="*" element={<NotFoundPage />} />
        </Routes>
      </Suspense>
    </BrowserRouter>
  )
}
