// src/lib/utils.ts - Ensure stable exports
import { type ClassValue, clsx } from "clsx"
import { twMerge } from "tailwind-merge"

// ✅ Stable function - won't cause re-renders
export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}

// ✅ Export other utilities if needed
export const formatDate = (date: Date): string => {
  return new Intl.DateTimeFormat('en-US', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  }).format(date)
}
