const currencyFormat = new Intl.NumberFormat("en-US", {
  currency: "USD",
  style: "currency",
  maximumFractionDigits: 7,
})

const COMPACT_TIERS = [
  { threshold: 1e12, suffix: "T" },
  { threshold: 1e9, suffix: "B" },
  { threshold: 1e6, suffix: "M" },
  { threshold: 1e3, suffix: "K" },
] as const

/**
 * Formats a USD amount in compact notation (e.g. 2_320_689_268_316 -> "$2.3T").
 *
 * Implemented manually instead of `Intl.NumberFormat({ notation: "compact" })`
 * because Hermes (iOS/Android) does not support compact notation and falls back
 * to the full number. This keeps the same "$2.3T" output on every platform.
 */
function formatCompactCurrency(value: number): string {
  if (!Number.isFinite(value)) return "$0.0"

  const sign = value < 0 ? "-" : ""
  const abs = Math.abs(value)
  const tier = COMPACT_TIERS.find((t) => abs >= t.threshold)

  if (!tier) return `${sign}$${abs.toFixed(1)}`

  return `${sign}$${(abs / tier.threshold).toFixed(1)}${tier.suffix}`
}

/**
 * Re-compacts an already-formatted USD string from the backend
 * (e.g. "$2,320,689,268,316" -> "$2.3T"), so large aggregate values never
 * overflow the row. Returns "—" for empty and the original string if it
 * cannot be parsed to a number.
 */
function compactUsdFromFormatted(formatted: string | null | undefined): string {
  if (formatted == null || formatted === "") return "—"

  const stripped = formatted.replace(/[^0-9.-]/g, "")
  if (stripped === "") return formatted

  const numeric = Number(stripped)
  if (!Number.isFinite(numeric)) return formatted

  return formatCompactCurrency(numeric)
}

export { currencyFormat, formatCompactCurrency, compactUsdFromFormatted }
