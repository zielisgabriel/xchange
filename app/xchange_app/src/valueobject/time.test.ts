import { Hour } from "./Hour"
import { Minute } from "./Minute"

describe("Time value objects", () => {
  it("converts hours to milliseconds", () => {
    expect(new Hour(1).toMilliseconds()).toBe(3_600_000)
    expect(new Hour(2).toMilliseconds()).toBe(7_200_000)
  })

  it("converts minutes to milliseconds", () => {
    expect(new Minute(1).toMilliseconds()).toBe(60_000)
    expect(new Minute(10).toMilliseconds()).toBe(600_000)
  })
})
