import { cn } from "./utils"

describe("cn", () => {
  it("merges class names", () => {
    expect(cn("px-2", "font-bold")).toBe("px-2 font-bold")
  })

  it("lets later Tailwind utilities win over conflicting earlier ones", () => {
    expect(cn("p-2", "p-4")).toBe("p-4")
  })

  it("ignores falsy values", () => {
    expect(cn("text-sm", false, null, undefined, "font-bold")).toBe("text-sm font-bold")
  })
})
