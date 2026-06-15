/**
 * Pure formatting helpers shared by forms. Kept free of React Native imports so
 * they can be unit-tested in isolation.
 */

/** Masks a raw CPF string progressively as `000.000.000-00`, capped at 11 digits. */
export function formatCpf(value: string): string {
  const digits = value.replace(/\D/g, "").slice(0, 11)

  if (digits.length <= 3) return digits
  if (digits.length <= 6) return `${digits.slice(0, 3)}.${digits.slice(3)}`
  if (digits.length <= 9) return `${digits.slice(0, 3)}.${digits.slice(3, 6)}.${digits.slice(6)}`
  return `${digits.slice(0, 3)}.${digits.slice(3, 6)}.${digits.slice(6, 9)}-${digits.slice(9)}`
}

/** Formats a date for display as `dd/mm/yyyy`. */
export function formatDateDisplay(date: Date): string {
  const day = String(date.getDate()).padStart(2, "0")
  const month = String(date.getMonth() + 1).padStart(2, "0")
  const year = date.getFullYear()
  return `${day}/${month}/${year}`
}

/** Formats a date as an ISO calendar date `yyyy-mm-dd` (local time). */
export function toISODate(date: Date): string {
  const day = String(date.getDate()).padStart(2, "0")
  const month = String(date.getMonth() + 1).padStart(2, "0")
  const year = date.getFullYear()
  return `${year}-${month}-${day}`
}
