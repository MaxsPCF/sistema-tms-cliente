import Card from '../../components/atoms/Card'
import Badge from '../../components/atoms/Badge'
import { useAuth } from '../../hooks/useAuth'
import type { AccesoPermisoModuloDto, PermisoAccion } from '../../types/auth.types'

const PERMISOS: { key: PermisoAccion; label: string }[] = [
  { key: 'puedeVer', label: 'Ver' },
  { key: 'puedeCrear', label: 'Crear' },
  { key: 'puedeEditar', label: 'Editar' },
  { key: 'puedeEliminar', label: 'Eliminar' },
  { key: 'puedeAprobar', label: 'Aprobar' },
  { key: 'puedeExportar', label: 'Exportar' },
]

function ModuleRow({ modulo, depth = 0 }: { modulo: AccesoPermisoModuloDto; depth?: number }) {
  return (
    <>
      <tr className="border-b border-gray-50 last:border-0">
        <td className="py-2.5 pr-4 text-sm text-gray-800" style={{ paddingLeft: depth * 20 }}>
          {modulo.nombreModulo}
          <span className="ml-2 text-xs text-gray-400">{modulo.ruta ?? '(agrupador)'}</span>
        </td>
        {PERMISOS.map((p) => (
          <td key={p.key} className="py-2.5 text-center">
            {modulo[p.key] ? (
              <Badge estado="APROBADO" label="Sí" className="bg-success-50 text-success-700" />
            ) : (
              <span className="text-xs text-gray-300">—</span>
            )}
          </td>
        ))}
      </tr>
      {modulo.children.map((child) => (
        <ModuleRow key={child.idModulo} modulo={child} depth={depth + 1} />
      ))}
    </>
  )
}

export default function DashboardPage() {
  const { usuario, menus } = useAuth()

  return (
    <div>
      <div className="mb-6">
        <h1 className="text-xl font-bold text-gray-900 sm:text-2xl">Hola, {usuario?.nombreUsuario} 👋</h1>
        <p className="mt-1 text-sm text-gray-500">
          Sesión iniciada como <strong>{usuario?.roles.join(', ')}</strong>. Este panel muestra el árbol de menús y
          permisos recibido del backend al iniciar sesión, mientras integramos los módulos de negocio.
        </p>
      </div>

      <Card className="overflow-x-auto p-5">
        <h2 className="mb-4 text-sm font-semibold text-gray-800">Menús y permisos (LoginResponseDto.Menus)</h2>
        <table className="w-full text-left">
          <thead>
            <tr className="border-b border-gray-100 text-xs uppercase tracking-wide text-gray-400">
              <th className="pb-2 pr-4 font-medium">Módulo</th>
              {PERMISOS.map((p) => (
                <th key={p.key} className="pb-2 text-center font-medium">
                  {p.label}
                </th>
              ))}
            </tr>
          </thead>
          <tbody>
            {[...menus]
              .sort((a, b) => a.orden - b.orden)
              .map((modulo) => (
                <ModuleRow key={modulo.idModulo} modulo={modulo} />
              ))}
          </tbody>
        </table>
      </Card>
    </div>
  )
}
