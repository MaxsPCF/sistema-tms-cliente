import type { ElementType, HTMLAttributes, ReactNode } from 'react'
import { cn } from '../../utils/cn'

interface CardProps extends HTMLAttributes<HTMLElement> {
  children: ReactNode
  as?: ElementType
}

export default function Card({ children, className, as: Component = 'div', ...props }: CardProps) {
  return (
    <Component className={cn('rounded-xl border border-gray-200 bg-white shadow-card', className)} {...props}>
      {children}
    </Component>
  )
}
