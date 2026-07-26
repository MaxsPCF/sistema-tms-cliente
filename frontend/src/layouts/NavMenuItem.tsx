import { useState, useMemo } from 'react'
import { NavLink, useLocation } from 'react-router-dom'
import { ChevronDown, Circle } from 'lucide-react'
import { useAppDispatch } from '../hooks/useAppRedux'
import { closeMobileSidebar } from '../context/uiSlice'
import { resolveIcon } from '../utils/resolveIcon'
import { cn } from '../utils/cn'
import type { AccesoPermisoModuloDto } from '../types/auth.types'

interface NavMenuItemProps {
  modulo: AccesoPermisoModuloDto
  collapsed: boolean
  depth?: number
}

/* ------------------------------------------------------------------ */
/*  Helpers                                                            */
/* ------------------------------------------------------------------ */

/** Retorna true si algún descendiente (a cualquier profundidad) tiene una ruta que coincide con el pathname */
function hasActiveDescendant(modulo: AccesoPermisoModuloDto, pathname: string): boolean {
  if (!modulo.children.length) return false
  return modulo.children.some((child) => {
    if (child.ruta && pathname.startsWith(child.ruta)) return true
    return hasActiveDescendant(child, pathname)
  })
}

/** Retorna true si el módulo o cualquiera de sus descendientes tiene `puedeVer === true` */
function hasVisibleContent(modulo: AccesoPermisoModuloDto): boolean {
  if (modulo.puedeVer) return true
  return modulo.children.some((child) => hasVisibleContent(child))
}

/* ------------------------------------------------------------------ */
/*  Componente                                                         */
/* ------------------------------------------------------------------ */

export default function NavMenuItem({ modulo, collapsed, depth = 0 }: NavMenuItemProps) {
  const dispatch = useAppDispatch()
  const location = useLocation()

  // Icono seguro: si es null o no existe, usamos Circle
  const Icon = useMemo(() => {
    if (!modulo.icono) return Circle
    return resolveIcon(modulo.icono) ?? Circle
  }, [modulo.icono])

  // ¿Tiene hijos que merezcan ser mostrados?
  const visibleChildren = useMemo(
    () => modulo.children.filter((child) => hasVisibleContent(child)),
    [modulo.children],
  )
  const hasChildren = visibleChildren.length > 0

  // ¿Algún descendiente está activo? (profundidad completa)
  const isChildActive = useMemo(
    () => hasActiveDescendant(modulo, location.pathname),
    [modulo, location.pathname],
  )

  const [expanded, setExpanded] = useState(isChildActive)

  // Si el módulo no es visible y no tiene hijos visibles, no se pinta nada
  if (!modulo.puedeVer && !hasChildren) return null

  // Agrupador con submenús (sin ruta propia navegable)
  if (hasChildren) {
    return (
      <div>
        <button
          onClick={() => setExpanded((v) => !v)}
          className={cn(
            'flex w-full items-center gap-3 rounded-lg px-3 py-2.5 text-sm font-medium text-gray-600 transition-colors hover:bg-gray-50 hover:text-gray-900',
            isChildActive && 'text-primary-700',
            collapsed && 'md:justify-center',
          )}
          title={collapsed ? modulo.nombreModulo : undefined}
        >
          <Icon className="h-5 w-5 shrink-0" />
          <span className={cn('flex-1 text-left', collapsed && 'md:hidden')}>
            {modulo.nombreModulo}
          </span>
          <ChevronDown
            className={cn(
              'h-4 w-4 shrink-0 transition-transform',
              expanded && 'rotate-180',
              collapsed && 'md:hidden',
            )}
          />
        </button>

        {expanded && (
          <div
            className={cn(
              'mt-1 space-y-1 border-l border-gray-100 pl-3',
              collapsed ? 'md:hidden' : 'ml-4',
            )}
          >
            {visibleChildren
              .slice()
              .sort((a, b) => a.orden - b.orden)
              .map((child) => (
                <NavMenuItem
                  key={child.idModulo}
                  modulo={child}
                  collapsed={collapsed}
                  depth={depth + 1}
                />
              ))}
          </div>
        )}
      </div>
    )
  }

  // Módulo hoja con ruta navegable
  return (
    <NavLink
      to={modulo.ruta ?? '#'}
      onClick={() => dispatch(closeMobileSidebar())}
      className={({ isActive }) =>
        cn(
          'flex items-center gap-3 rounded-lg px-3 py-2.5 text-sm font-medium transition-colors',
          isActive
            ? 'bg-primary-50 text-primary-700'
            : 'text-gray-600 hover:bg-gray-50 hover:text-gray-900',
          collapsed && 'md:justify-center',
        )
      }
      title={collapsed ? modulo.nombreModulo : undefined}
    >
      <Icon className="h-5 w-5 shrink-0" />
      <span className={cn(collapsed && 'md:hidden')}>{modulo.nombreModulo}</span>
    </NavLink>
  )
}