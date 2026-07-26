import { Link } from 'react-router-dom'

/**
 * Placeholder temporal: aún no compartiste el contrato del backend para el
 * registro de clientes. Cuando lo tengas, migramos esta pantalla igual que
 * hicimos con el login (DTOs tipados + validación Zod acorde al contrato).
 */
export default function RegisterPage() {
  return (
    <div className="text-center">
      <h1 className="text-2xl font-bold text-gray-900">Solicitud de registro</h1>
      <p className="mt-3 text-sm text-gray-500">
        Este módulo está pendiente de implementar según el contrato del backend correspondiente.
      </p>
      <Link to="/login" className="mt-6 inline-block text-sm font-medium text-primary-600 hover:text-primary-700">
        Volver al login
      </Link>
    </div>
  )
}
