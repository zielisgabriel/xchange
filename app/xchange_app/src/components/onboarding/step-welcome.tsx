import { View } from "react-native"
import { Text } from "@/components/ui/text"
import { Icon } from "@/components/ui/icon"
import { TrendingUp, ChartLine, Shield } from "lucide-react-native"
import Animated, { FadeIn, FadeInDown } from "react-native-reanimated"

const FEATURES = [
  {
    icon: TrendingUp,
    title: "Acompanhe o mercado",
    description: "Cotações em tempo real das principais criptos",
    color: { bg: "bg-emerald-500/10", text: "text-emerald-400" },
  },
  {
    icon: ChartLine,
    title: "Análises detalhadas",
    description: "Gráficos e dados de mercado completos",
    color: { bg: "bg-blue-500/10", text: "text-blue-400" },
  },
  {
    icon: Shield,
    title: "Segurança total",
    description: "Seus dados protegidos e criptografados",
    color: { bg: "bg-violet-500/10", text: "text-violet-400" },
  },
]

export function StepWelcome() {
  return (
    <View className="flex-1 justify-between px-6 pt-20 pb-6">
      <Animated.View
        entering={FadeInDown.delay(200).duration(600)}
        className="items-center mt-16"
      >
        <Text className="text-foreground font-sora-extrabold tracking-widest text-4xl">
          XCHANGE
        </Text>
        <Text className="text-foreground/50 font-light text-sm text-center mt-2 leading-5">
          Plataforma para investidores de cripto
        </Text>
      </Animated.View>

      <Animated.View
        entering={FadeInDown.delay(500).duration(600)}
        className="gap-5 px-2"
      >
        {FEATURES.map((feature) => (
          <View key={feature.title} className="flex-row items-center gap-4">
            <View className={`w-11 h-11 rounded-2xl ${feature.color.bg} items-center justify-center`}>
              <Icon as={feature.icon} className={`size-5 ${feature.color.text}`} />
            </View>
            <View className="flex-1">
              <Text className="text-sm font-sora-semibold text-foreground">
                {feature.title}
              </Text>
              <Text className="text-xs text-foreground/40 mt-0.5">
                {feature.description}
              </Text>
            </View>
          </View>
        ))}
      </Animated.View>

      <Animated.View entering={FadeIn.delay(800).duration(400)}>
        <Text className="text-foreground/20 text-center text-[11px] leading-4">
          Deslize para configurar sua experiência
        </Text>
      </Animated.View>
    </View>
  )
}
