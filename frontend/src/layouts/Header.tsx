import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Menu, Bell, LogOut, ChevronDown, User } from 'lucide-react'
import { useAppDispatch } from '../hooks/useAppRedux'
import { toggleMobileSidebar } from '../context/uiSlice'
import { useAuth } from '../hooks/useAuth'

export default function Header() {
  const dispatch = useAppDispatch()
  const { usuario, logout } = useAuth()
  const navigate = useNavigate()
  const [menuOpen, setMenuOpen] = useState(false)
  const [notifOpen, setNotifOpen] = useState(false)

  function handleLogout() {
    logout()
    navigate('/login')
  }

  return (
    <header className="sticky top-0 z-20 flex h-16 items-center justify-between border-b border-gray-200 bg-white/95 px-4 backdrop-blur sm:px-6">
      <button
        className="rounded-lg p-2 text-gray-500 hover:bg-gray-100 md:hidden"
        onClick={() => dispatch(toggleMobileSidebar())}
        aria-label="Abrir menú"
      >
        <Menu className="h-5 w-5" />
      </button>

      <div className="hidden md:block" />

      <div className="flex items-center gap-2 sm:gap-4">
        <div className="relative">
          <button
            onClick={() => setNotifOpen((v) => !v)}
            className="relative rounded-lg p-2 text-gray-500 hover:bg-gray-100"
            aria-label="Notificaciones"
          >
            <Bell className="h-5 w-5" />
            <span className="absolute right-1.5 top-1.5 h-2 w-2 rounded-full bg-danger-500" />
          </button>
          {notifOpen && (
            <div className="absolute right-0 mt-2 w-72 rounded-xl border border-gray-100 bg-white p-3 shadow-lg">
              <p className="mb-2 text-xs font-semibold uppercase text-gray-400">Notificaciones</p>
              <div className="space-y-2 text-sm text-gray-600">
                <p className="rounded-lg bg-gray-50 p-2">Aún no tienes notificaciones nuevas.</p>
              </div>
            </div>
          )}
        </div>

        <div className="relative">
          <button onClick={() => setMenuOpen((v) => !v)} className="flex items-center gap-2 rounded-lg px-2 py-1.5 hover:bg-gray-100">
            <div className="flex h-8 w-8 items-center justify-center rounded-full bg-primary-100 text-primary-700">
              <User className="h-4 w-4" />
            </div>
            <span className="hidden text-sm font-medium text-gray-700 sm:block">{usuario?.nombreUsuario ?? 'Usuario'}</span>
            <ChevronDown className="hidden h-4 w-4 text-gray-400 sm:block" />
          </button>
          {menuOpen && (
            <div className="absolute right-0 mt-2 w-56 rounded-xl border border-gray-100 bg-white py-1 shadow-lg">
              <div className="border-b border-gray-100 px-4 py-3">
                <p className="text-sm font-medium text-gray-800">{usuario?.nombreUsuario}</p>
                <p className="truncate text-xs text-gray-500">{usuario?.email}</p>
                {usuario?.roles && usuario.roles.length > 0 && (
                  <p className="mt-1 text-xs text-gray-400">{usuario.roles.join(', ')}</p>
                )}
              </div>
              <button
                onClick={handleLogout}
                className="flex w-full items-center gap-2 px-4 py-2.5 text-sm text-danger-600 hover:bg-danger-50"
              >
                <LogOut className="h-4 w-4" />
                Cerrar sesión
              </button>
            </div>
          )}
        </div>
      </div>
    </header>
  )
}
