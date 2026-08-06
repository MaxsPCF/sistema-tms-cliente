import { cn } from '../../utils/cn'

const ESTADO_STYLES: Record<string, string> = {
  PENDIENTE: 'bg-gray-100 text-gray-700',
  EN_COTIZACION: 'bg-warning-50 text-warning-700',
  COTIZADO: 'bg-primary-100 text-primary-700',
  APROBADO: 'bg-success-50 text-success-700',
  ANULADO: 'bg-danger-50 text-danger-700',
  ANULADA: 'bg-danger-50 text-danger-700',
  RECHAZADO: 'bg-danger-50 text-danger-700',
  VENCIDO: 'bg-gray-200 text-gray-600',
  EN_PROCESO: 'bg-primary-100 text-primary-700',
  COMPLETADA: 'bg-success-50 text-success-700',
  COMPLETADO: 'bg-success-50 text-success-700',
  PROGRAMADO: 'bg-secondary-100 text-secondary-700',
  EN_CURSO: 'bg-warning-50 text-warning-700',
}

interface BadgeProps {
  estado: string
  label?: string
  className?: string
}

export default function Badge({ estado, label, className }: BadgeProps) {
  const style = ESTADO_STYLES[estado] || 'bg-gray-100 text-gray-700'
  return (
    <span className={cn('inline-flex items-center rounded-full px-2.5 py-1 text-xs font-medium', style, className)}>
      {label || estado}
    </span>
  )
}
