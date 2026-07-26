import { forwardRef, type InputHTMLAttributes } from 'react'
import type { LucideIcon } from 'lucide-react'
import { cn } from '../../utils/cn'

interface InputProps extends InputHTMLAttributes<HTMLInputElement> {
  label?: string
  error?: string
  hint?: string
  icon?: LucideIcon
}

const Input = forwardRef<HTMLInputElement, InputProps>(({ label, error, hint, className, icon: Icon, id, ...props }, ref) => {
  const inputId = id || props.name

  return (
    <div className="w-full">
      {label && (
        <label htmlFor={inputId} className="mb-1.5 block text-sm font-medium text-gray-700">
          {label}
        </label>
      )}
      <div className="relative">
        {Icon && <Icon className="pointer-events-none absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-gray-400" />}
        <input
          id={inputId}
          ref={ref}
          className={cn(
            'w-full rounded-lg border bg-white px-3.5 py-2.5 text-sm text-gray-900 placeholder:text-gray-400',
            'focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-primary-500',
            'transition-colors disabled:bg-gray-50 disabled:text-gray-400',
            Icon && 'pl-9',
            error ? 'border-danger-400 focus:ring-danger-400 focus:border-danger-400' : 'border-gray-300',
            className
          )}
          {...props}
        />
      </div>
      {error && <p className="mt-1.5 text-xs text-danger-600">{error}</p>}
      {hint && !error && <p className="mt-1.5 text-xs text-gray-500">{hint}</p>}
    </div>
  )
})

Input.displayName = 'Input'
export default Input
