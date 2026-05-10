import { TrendingCoins } from "@/components/trending-coins"
import { SectionHeader } from "@/components/section-header"
import { Text } from "@/components/ui/text"
import { Icon } from "@/components/ui/icon"
import {
  TrendingUp,
  Search,
  Bell
} from "lucide-react-native"
import { Pressable, RefreshControl, ScrollView, View } from "react-native"
import { LinearGradient } from "expo-linear-gradient"
import Animated, { FadeInDown, FadeIn } from "react-native-reanimated"
import { useQueryClient } from "@tanstack/react-query"
import { useCallback, useState } from "react"
import { GlobalCoinsHeader } from "@/components/global-coins-header"
import { useAuthStore } from "@/hooks/use-auth-store"
import { useProfileSimple } from "@/hooks/use-profile-simple"
import { Skeleton } from "@/components/ui/skeleton"

function getGreeting(): string {
  const hour = new Date().getHours()
  if (hour < 12) return "Bom dia"
  if (hour < 18) return "Boa tarde"
  return "Boa noite"
}

export default function Index() {
  const queryClient = useQueryClient()
  const [refreshing, setRefreshing] = useState(false)
  const { data: profileSimple, isLoading, isError } = useProfileSimple()

  const onRefresh = useCallback(async () => {
    setRefreshing(true)
    await queryClient.invalidateQueries({ queryKey: ["trending-coins"] })
    await new Promise((resolve) => setTimeout(resolve, 800))
    setRefreshing(false)
  }, [queryClient])

  return (
    <ScrollView
      className="flex-1 bg-background"
      contentInsetAdjustmentBehavior="automatic"
      showsVerticalScrollIndicator={false}
      refreshControl={
        <RefreshControl
          refreshing={refreshing}
          onRefresh={onRefresh}
          tintColor="#a1a1aa"
          colors={["#a1a1aa"]}
          progressBackgroundColor="#18181b"
        />
      }
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
            <Text className="text-foreground/50 text-sm font-medium">
              {getGreeting()} 👋
            </Text>
            {isLoading ? (
              <Skeleton className="h-8 w-32" />
            ) : (
              <Text className="text-foreground text-xl font-bold tracking-tight">
                {isError ? "Usuário" : profileSimple?.firstName}
              </Text>
            )}
          </View>

          <Pressable className="w-10 h-10 rounded-full bg-white/10 items-center justify-center active:bg-white/20">
            <Icon as={Bell} className="size-5 text-foreground/70" />
          </Pressable>
        </Animated.View>

        <Animated.View entering={FadeInDown.delay(100).duration(500)}>
          <Pressable className="flex-row items-center gap-2.5 bg-white/8 rounded-2xl px-4 py-3 mt-5">
            <Icon as={Search} className="size-4 text-foreground/40" />
            <Text className="text-foreground/30 text-sm">
              Buscar criptomoedas...
            </Text>
          </Pressable>
        </Animated.View>
      </LinearGradient>

      <GlobalCoinsHeader />

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
  )
}