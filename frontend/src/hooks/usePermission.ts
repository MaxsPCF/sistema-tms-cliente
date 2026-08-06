import { useMemo } from 'react'
import { useAppSelector } from './useAppRedux'
import type { AccesoPermisoModuloDto } from '../types/auth.types'

/**
 * Busca recursivamente un módulo dentro del árbol de menús por su ruta exacta.
 */
function findModuleByRoute(nodes: AccesoPermisoModuloDto[], ruta: string): AccesoPermisoModuloDto | null {
  for (const node of nodes) {
    if (node.ruta === ruta) return node
    if (node.children.length > 0) {
      const found = findModuleByRoute(node.children, ruta)
      if (found) return found
    }
  }
  return null
}

/**
 * Devuelve los permisos (ver/crear/editar/eliminar/aprobar/exportar) del módulo
 * asociado a una ruta, según el menú entregado por el backend al hacer login.
 * Si el módulo no existe en el árbol, todos los permisos se consideran `false`.
 */
export function usePermission(ruta: string) {
  const menus = useAppSelector((state) => state.auth.menus)

  return useMemo(() => {
    const module = findModuleByRoute(menus, ruta)
    return {
      puedeVer: module?.puedeVer ?? false,
      puedeCrear: module?.puedeCrear ?? false,
      puedeEditar: module?.puedeEditar ?? false,
      puedeEliminar: module?.puedeEliminar ?? false,
      puedeAprobar: module?.puedeAprobar ?? false,
      puedeExportar: module?.puedeExportar ?? false,
    }
  }, [menus, ruta])
}
