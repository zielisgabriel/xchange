import { formatCpf, formatDateDisplay, toISODate } from "./format"

describe("formatCpf", () => {
  it("masks a full CPF as 000.000.000-00", () => {
    expect(formatCpf("12345678901")).toBe("123.456.789-01")
  })

  it("masks progressively as the user types", () => {
    expect(formatCpf("123")).toBe("123")
    expect(formatCpf("1234")).toBe("123.4")
    expect(formatCpf("1234567")).toBe("123.456.7")
    expect(formatCpf("1234567890")).toBe("123.456.789-0")
  })

  it("strips non-digits and caps at 11 digits", () => {
    expect(formatCpf("abc123.456")).toBe("123.456")
    expect(formatCpf("123456789012345")).toBe("123.456.789-01")
  })
})

describe("toISODate", () => {
  it("formats as yyyy-mm-dd with zero padding", () => {
    expect(toISODate(new Date(2005, 7, 9))).toBe("2005-08-09")
    expect(toISODate(new Date(2024, 11, 31))).toBe("2024-12-31")
  })
})

describe("formatDateDisplay", () => {
  it("formats as dd/mm/yyyy with zero padding", () => {
    expect(formatDateDisplay(new Date(2005, 7, 9))).toBe("09/08/2005")
    expect(formatDateDisplay(new Date(2024, 0, 1))).toBe("01/01/2024")
  })
})
