import { View } from "react-native"
import { Text } from "@/components/ui/text"
import { Icon } from "@/components/ui/icon"
import { Button } from "@/components/ui/button"
import { LinearGradient } from "expo-linear-gradient"
import { ArrowLeft, ArrowRight, Rocket } from "lucide-react-native"
import { useAuthStore } from "@/hooks/use-auth-store"
import { useState } from "react"
import Animated, {
  FadeIn,
  SlideInRight,
  SlideOutLeft,
  SlideInLeft,
  SlideOutRight,
} from "react-native-reanimated"
import { apiFetch } from "@/lib/api-fetch"
import { FavoriteCoin } from "@/types/favorite-coin"
import { StepWelcome } from "@/components/onboarding/step-welcome"
import { StepCryptos } from "@/components/onboarding/step-cryptos"
import { StepFinish } from "@/components/onboarding/step-finish"
import { onboardingSchema } from "@/schemas/onboarding-schema"
import { toast } from "sonner-native"
import { queryClient } from "@/lib/query-client"

const TOTAL_STEPS = 3

export default function Onboarding() {
  const { finishOnboarding } = useAuthStore()
  const [currentStep, setCurrentStep] = useState(0)
  const [selectedCryptos, setSelectedCryptos] = useState<FavoriteCoin[]>([])
  const [direction, setDirection] = useState<"forward" | "backward">("forward")
  const [error, setError] = useState<string | null>(null)

  async function submitOnboarding() {
    const payload = { favorite_coins: selectedCryptos }

    const result = onboardingSchema.safeParse(payload)
    if (!result.success) {
      const message = result.error.issues[0].message
      setError(message)
      toast.error(message)
      setDirection("backward")
      setCurrentStep(1)
      return
    }

    const response = await apiFetch({
      input: "/api/profile/onboarding",
      init: {
        method: "POST",
        body: JSON.stringify(result.data),
      },
    })

    if (!response.ok) {
      const data = await response.json().catch(() => null)
      const message = data?.fieldErrors?.[0]?.message ?? data?.message ?? "Erro ao salvar"
      setError(message)
      toast.error(message)
      setDirection("backward")
      setCurrentStep(1)
      return
    }

    queryClient.setQueryData(["profile-simple"], (old: any) => {
      if (!old) return old
      return {
        ...old,
        onboardingFinished: true,
        favoriteCoins: selectedCryptos
      }
    })

    finishOnboarding()
  }

  function handleNext() {
    if (currentStep === TOTAL_STEPS - 1) {
      submitOnboarding()
      return
    }
    setDirection("forward")
    setCurrentStep((prev) => prev + 1)
  }

  function handleBack() {
    setDirection("backward")
    setCurrentStep((prev) => prev - 1)
  }

  function toggleCrypto(coin: FavoriteCoin) {
    setError(null)
    setSelectedCryptos((prev) => {
      const exists = prev.some((c) => c.coinId === coin.coinId)
      if (exists) return prev.filter((c) => c.coinId !== coin.coinId)
      if (prev.length >= 5) return prev
      return [...prev, coin]
    })
  }

  const enteringAnim =
    direction === "forward"
      ? SlideInRight.duration(400).springify()
      : SlideInLeft.duration(400).springify()

  const exitingAnim =
    direction === "forward"
      ? SlideOutLeft.duration(300)
      : SlideOutRight.duration(300)

  const isLastStep = currentStep === TOTAL_STEPS - 1

  return (
    <LinearGradient
      colors={["hsl(0, 0%, 8%)", "hsl(0, 0%, 2%)"]}
      className="flex-1"
    >
      <View className="flex-1">
        <Animated.View
          key={currentStep}
          entering={enteringAnim}
          exiting={exitingAnim}
          className="flex-1"
        >
          {currentStep === 0 && <StepWelcome />}
          {currentStep === 1 && (
            <StepCryptos selected={selectedCryptos} onToggle={toggleCrypto} error={error} />
          )}
          {currentStep === 2 && <StepFinish />}
        </Animated.View>

        <View className="px-6 pb-10 pt-4">
          <Animated.View
            entering={FadeIn.duration(400)}
            className="flex-row items-center justify-center gap-2 mb-6"
          >
            {Array.from({ length: TOTAL_STEPS }).map((_, i) => (
              <View
                key={i}
                className={`h-1 rounded-full ${
                  i === currentStep
                    ? "w-6 bg-foreground"
                    : i < currentStep
                      ? "w-2 bg-foreground/40"
                      : "w-2 bg-foreground/15"
                }`}
              />
            ))}
          </Animated.View>

          <View className="flex-row gap-3">
            {currentStep > 0 ? (
              <Button
                variant="outline"
                size="lg"
                onPress={handleBack}
                className="flex-1 border-white/10 bg-white/5"
              >
                <Icon as={ArrowLeft} className="size-4 text-foreground" />
                <Text className="text-foreground">Voltar</Text>
              </Button>
            ) : (
              <View className="flex-1" />
            )}

            <Button
              variant="default"
              size="lg"
              onPress={handleNext}
              className="flex-1"
            >
              <Text>{isLastStep ? "Começar" : "Próximo"}</Text>
              <Icon
                as={isLastStep ? Rocket : ArrowRight}
                className="size-4 text-primary-foreground"
              />
            </Button>
          </View>
        </View>
      </View>
    </LinearGradient>
  )
}
