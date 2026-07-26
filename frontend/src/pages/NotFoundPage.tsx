import { Link } from 'react-router-dom'
import { Compass } from 'lucide-react'
import Button from '../components/atoms/Button'

export default function NotFoundPage() {
  return (
    <div className="flex min-h-screen flex-col items-center justify-center bg-gray-50 px-4 text-center">
      <div className="mb-4 flex h-16 w-16 items-center justify-center rounded-full bg-primary-50">
        <Compass className="h-8 w-8 text-primary-600" />
      </div>
      <h1 className="text-3xl font-bold text-gray-900">404</h1>
      <p className="mt-2 text-sm text-gray-500">La página que buscas no existe o fue movida.</p>
      <Link to="/dashboard" className="mt-6">
        <Button>Volver al inicio</Button>
      </Link>
    </div>
  )
}
