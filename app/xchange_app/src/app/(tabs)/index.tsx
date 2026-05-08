import { TrendingCoins } from "@/components/trending-coins";
import { SectionHeader } from "@/components/section-header";
import { Text } from "@/components/ui/text";
import { IconBadge } from "@/components/ui/icon-badge";
import { Icon } from "@/components/ui/icon";
import {
  TrendingUp,
  Search,
  Bell,
  ArrowUpRight,
  ArrowDownRight,
  BarChart3,
  Activity,
} from "lucide-react-native";
import { Pressable, ScrollView, View } from "react-native";
import { LinearGradient } from "expo-linear-gradient";
import Animated, { FadeInDown, FadeIn } from "react-native-reanimated";

function getGreeting(): string {
  const hour = new Date().getHours();
  if (hour < 12) return "Bom dia";
  if (hour < 18) return "Boa tarde";
  return "Boa noite";
}

export default function Index() {
  return (
    <ScrollView
      className="flex-1 bg-background"
      contentInsetAdjustmentBehavior="automatic"
      showsVerticalScrollIndicator={false}
    >
      <LinearGradient
        colors={["hsl(0, 0%, 12%)", "hsl(0, 0%, 5%)"]}
        start={{ x: 0, y: 0 }}
        end={{ x: 0.5, y: 1 }}
        className="pt-14 pb-9 px-4 rounded-b-[28px]"
      >
        <Animated.View
          entering={FadeIn.duration(600)}
          className="flex-row items-center justify-between"
        >
          <View className="gap-1">
            <Text className="text-white/50 text-sm font-medium">
              {getGreeting()} 👋
            </Text>
            <Text className="text-white text-xl font-bold tracking-tight">
              Usuário
            </Text>
          </View>

          <Pressable className="w-10 h-10 rounded-full bg-white/10 items-center justify-center active:bg-white/20">
            <Icon as={Bell} className="size-5 text-white/70" />
          </Pressable>
        </Animated.View>

        <Animated.View entering={FadeInDown.delay(100).duration(500)}>
          <Pressable className="flex-row items-center gap-2.5 bg-white/8 rounded-2xl px-4 py-3 mt-5">
            <Icon as={Search} className="size-4 text-white/40" />
            <Text className="text-white/30 text-sm">
              Buscar criptomoedas...
            </Text>
          </Pressable>
        </Animated.View>
      </LinearGradient>

      <Animated.View
        entering={FadeInDown.delay(200).duration(500).springify().damping(16)}
        className="px-4 -mt-6"
      >
        <View className="flex-row gap-3">
          <View className="flex-1 bg-card rounded-2xl p-4 gap-2 border border-border/50 shadow-sm">
            <View className="flex-row items-center gap-1.5">
              <IconBadge icon={BarChart3} variant="success" size="sm" />
              <Text className="text-xs text-muted-foreground font-medium">
                Market Cap
              </Text>
            </View>
            <Text className="text-lg font-bold">$3.2T</Text>
            <View className="flex-row items-center gap-0.5">
              <ArrowUpRight size={12} color="#22c55e" />
              <Text className="text-xs font-medium" style={{ color: "#22c55e" }}>
                +2.4%
              </Text>
            </View>
          </View>

          <View className="flex-1 bg-card rounded-2xl p-4 gap-2 border border-border/50 shadow-sm">
            <View className="flex-row items-center gap-1.5">
              <IconBadge icon={Activity} variant="info" size="sm" />
              <Text className="text-xs text-muted-foreground font-medium">
                Volume 24h
              </Text>
            </View>
            <Text className="text-lg font-bold">$128B</Text>
            <View className="flex-row items-center gap-0.5">
              <ArrowDownRight size={12} color="#ef4444" />
              <Text className="text-xs font-medium" style={{ color: "#ef4444" }}>
                -1.1%
              </Text>
            </View>
          </View>
        </View>
      </Animated.View>

      <Animated.View
        entering={FadeInDown.delay(300).duration(500)}
        className="px-4 mt-6"
      >
        <SectionHeader
          icon={TrendingUp}
          title="Tendências"
          subtitle="Mais populares agora"
        />
        <TrendingCoins />
      </Animated.View>

      <View className="h-8" />
    </ScrollView>
  );
}