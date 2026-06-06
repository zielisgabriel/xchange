import { z } from "zod"

const favoriteCoinSchema = z.object({
  coinId: z.string().min(1),
  name: z.string().min(1),
  symbol: z.string().min(1),
})

export const onboardingSchema = z.object({
  favorite_coins: z
    .array(favoriteCoinSchema)
    .min(1, "Selecione pelo menos 1 criptomoeda")
    .max(5, "Selecione no máximo 5 criptomoedas"),
})

export type OnboardingPayload = z.infer<typeof onboardingSchema>
