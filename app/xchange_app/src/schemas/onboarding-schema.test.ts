import { onboardingSchema } from "./onboarding-schema"

const coin = (coinId: string) => ({ coinId, name: coinId, symbol: coinId.toUpperCase() })

describe("onboardingSchema", () => {
  it("accepts between 1 and 5 favorite coins", () => {
    expect(onboardingSchema.safeParse({ favorite_coins: [coin("bitcoin")] }).success).toBe(true)
    expect(
      onboardingSchema.safeParse({
        favorite_coins: ["a", "b", "c", "d", "e"].map(coin),
      }).success
    ).toBe(true)
  })

  it("rejects an empty selection", () => {
    const result = onboardingSchema.safeParse({ favorite_coins: [] })
    expect(result.success).toBe(false)
    if (!result.success) {
      expect(result.error.issues[0].message).toBe("Selecione pelo menos 1 criptomoeda")
    }
  })

  it("rejects more than 5 coins", () => {
    const result = onboardingSchema.safeParse({
      favorite_coins: ["a", "b", "c", "d", "e", "f"].map(coin),
    })
    expect(result.success).toBe(false)
    if (!result.success) {
      expect(result.error.issues[0].message).toBe("Selecione no máximo 5 criptomoedas")
    }
  })

  it("rejects coins missing required fields", () => {
    const result = onboardingSchema.safeParse({
      favorite_coins: [{ coinId: "bitcoin", name: "", symbol: "BTC" }],
    })
    expect(result.success).toBe(false)
  })
})
