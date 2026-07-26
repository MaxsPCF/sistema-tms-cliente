import { Truck, PanelLeftClose, PanelLeftOpen, X } from 'lucide-react'
import { useAppDispatch, useAppSelector } from '../hooks/useAppRedux'
import { closeMobileSidebar, toggleSidebarCollapsed } from '../context/uiSlice'
import { cn } from '../utils/cn'
import { ENV } from '../config/env'
import NavMenuItem from './NavMenuItem'

/**
 * Sidebar de navegación principal.
 * El árbol de módulos (`menus`) viene del backend en la respuesta de login
 * (LoginResponseDto.Menus) y ya incluye los permisos por módulo (`puedeVer`, etc.).
 * - Mobile: panel deslizable (drawer) sobre el contenido.
 * - Tablet/Desktop: sidebar fijo, colapsable a solo íconos.
 */
export default function Sidebar() {
  const dispatch = useAppDispatch()
  const { sidebarOpen, sidebarCollapsed } = useAppSelector((state) => state.ui)
  const menus = useAppSelector((state) => state.auth.menus)

  const menusOrdenados = [...menus].sort((a, b) => a.orden - b.orden)

  return (
    <>
      {sidebarOpen && (
        <div
          className="fixed inset-0 z-30 bg-gray-900/50 md:hidden"
          onClick={() => dispatch(closeMobileSidebar())}
          aria-hidden="true"
        />
      )}

      <aside
        className={cn(
          'fixed inset-y-0 left-0 z-40 flex flex-col border-r border-gray-200 bg-white transition-all duration-200',
          'w-72 -translate-x-full md:translate-x-0',
          sidebarOpen && 'translate-x-0',
          'md:static md:z-0',
          sidebarCollapsed ? 'md:w-20' : 'md:w-64'
        )}
      >
        <div className="flex h-16 items-center justify-between border-b border-gray-100 px-4">
          <div className="flex items-center gap-2 overflow-hidden">
            <div className="flex h-9 w-9 shrink-0 items-center justify-center rounded-lg bg-primary-600 text-white">
              <Truck className="h-5 w-5" />
            </div>
            {!sidebarCollapsed && <span className="truncate font-semibold text-gray-900">{ENV.APP_NAME}</span>}
          </div>
          <button
            className="rounded-lg p-1.5 text-gray-400 hover:bg-gray-100 md:hidden"
            onClick={() => dispatch(closeMobileSidebar())}
            aria-label="Cerrar menú"
          >
            <X className="h-5 w-5" />
          </button>
        </div>

        <nav className="flex-1 space-y-1 overflow-y-auto px-3 py-4">
          {menusOrdenados.map((modulo) => (
            <NavMenuItem key={modulo.idModulo} modulo={modulo} collapsed={sidebarCollapsed} />
          ))}
        </nav>

        <button
          onClick={() => dispatch(toggleSidebarCollapsed())}
          className="hidden items-center gap-2 border-t border-gray-100 px-4 py-3 text-xs font-medium text-gray-500 hover:bg-gray-50 md:flex"
        >
          {sidebarCollapsed ? <PanelLeftOpen className="h-4 w-4" /> : <PanelLeftClose className="h-4 w-4" />}
          {!sidebarCollapsed && 'Colapsar menú'}
        </button>
      </aside>
    </>
  )
}
