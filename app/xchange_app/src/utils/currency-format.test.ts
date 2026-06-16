import { compactUsdFromFormatted, currencyFormat, formatCompactCurrency } from "./currency-format"

describe("currencyFormat", () => {
  it("formats whole values as USD currency", () => {
    expect(currencyFormat.format(1000)).toBe("$1,000.00")
  })

  it("keeps up to 7 fraction digits for small values", () => {
    expect(currencyFormat.format(0.1234567)).toBe("$0.1234567")
  })
})

describe("formatCompactCurrency", () => {
  it("formats trillions / billions / millions / thousands compactly", () => {
    expect(formatCompactCurrency(2_320_689_268_316.1)).toBe("$2.3T")
    expect(formatCompactCurrency(45_000_000_000)).toBe("$45.0B")
    expect(formatCompactCurrency(7_500_000)).toBe("$7.5M")
    expect(formatCompactCurrency(12_300)).toBe("$12.3K")
  })

  it("formats values below 1000 without a suffix", () => {
    expect(formatCompactCurrency(500)).toBe("$500.0")
    expect(formatCompactCurrency(0)).toBe("$0.0")
  })

  it("keeps the sign for negative values", () => {
    expect(formatCompactCurrency(-2_320_689_268_316.1)).toBe("-$2.3T")
  })

  it("falls back to $0.0 for non-finite input", () => {
    expect(formatCompactCurrency(Number.NaN)).toBe("$0.0")
  })
})

describe("compactUsdFromFormatted", () => {
  it("compacts a full backend-formatted currency string", () => {
    expect(compactUsdFromFormatted("$2,320,689,268,316")).toBe("$2.3T")
    expect(compactUsdFromFormatted("$95,000,000,000.50")).toBe("$95.0B")
  })

  it("returns an em dash for empty/nullish values", () => {
    expect(compactUsdFromFormatted(null)).toBe("—")
    expect(compactUsdFromFormatted("")).toBe("—")
  })

  it("returns the original string when it cannot be parsed", () => {
    expect(compactUsdFromFormatted("N/A")).toBe("N/A")
  })
})
