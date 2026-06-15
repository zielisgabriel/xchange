import { currencyFormat } from "./currency-format"

describe("currencyFormat", () => {
  it("formats whole values as USD currency", () => {
    expect(currencyFormat.format(1000)).toBe("$1,000.00")
  })

  it("keeps up to 7 fraction digits for small values", () => {
    expect(currencyFormat.format(0.1234567)).toBe("$0.1234567")
  })
})
