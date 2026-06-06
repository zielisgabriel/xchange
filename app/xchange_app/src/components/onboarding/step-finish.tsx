import { View } from "react-native"
import { Text } from "@/components/ui/text"
import { Icon } from "@/components/ui/icon"
import { Rocket } from "lucide-react-native"
import Animated, { FadeIn, FadeInUp } from "react-native-reanimated"

export function StepFinish() {
  return (
    <View className="flex-1 justify-center items-center px-8">
      <Animated.View
        entering={FadeInUp.delay(200).duration(600).springify()}
        className="items-center"
      >
        <View className="w-24 h-24 rounded-[28px] bg-emerald-500/10 items-center justify-center mb-8 border border-emerald-500/15">
          <Icon as={Rocket} className="size-12 text-emerald-400" />
        </View>

        <Text className="text-foreground font-sora-bold text-2xl text-center mb-3">
          Tudo pronto!
        </Text>
        <Text className="text-foreground/40 text-sm text-center leading-5 max-w-[280px]">
          Sua experiência está configurada. Vamos começar a explorar o mundo
          cripto juntos.
        </Text>
      </Animated.View>

      <Animated.View
        entering={FadeIn.delay(700).duration(500)}
        className="mt-12 flex-row items-center gap-2 bg-white/5 rounded-full px-5 py-2.5 border border-white/8"
      >
        <View className="w-2 h-2 rounded-full bg-emerald-400" />
        <Text className="text-foreground/50 text-xs font-sora-medium">
          Clique em começar para entrar
        </Text>
      </Animated.View>
    </View>
  )
}
