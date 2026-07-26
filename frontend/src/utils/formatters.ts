import { format, parseISO } from 'date-fns'
import { es } from 'date-fns/locale'

export function formatDate(isoDate?: string | null, pattern = 'dd/MM/yyyy'): string {
  if (!isoDate) return '—'
  try {
    return format(parseISO(isoDate), pattern, { locale: es })
  } catch {
    return '—'
  }
}

export function formatDateTime(isoDate?: string | null): string {
  return formatDate(isoDate, "dd/MM/yyyy HH:mm")
}
