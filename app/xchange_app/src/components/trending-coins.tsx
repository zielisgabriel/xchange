import { CoinWithMarketData } from "@/types/coin-with-market-data"
import { Text } from "./ui/text"
import { useQuery } from "@tanstack/react-query"
import { Pressable, View } from "react-native"
import { Button } from "./ui/button"
import { Avatar, AvatarImage } from "./ui/avatar"
import { Image } from "expo-image"
import { useState } from "react"
import { ChevronDown, ChevronUp } from "lucide-react-native"
import { useRouter } from "expo-router"
import { Skeleton } from "./ui/skeleton"
import Animated, { FadeInDown } from "react-native-reanimated"

interface TrendingCoinResponse {
  coins: CoinWithMarketData[]
}

const currencyFormat = new Intl.NumberFormat("en-US", {
  currency: "USD",
  style: "currency",
  maximumFractionDigits: 7,
})

const percentageFormat = new Intl.NumberFormat("pt-BR", {
  style: "percent",
  minimumFractionDigits: 2,
  maximumFractionDigits: 2,
})

const sparklineStyle = { width: 80, height: 32 }

export function TrendingCoins() {
  const [showMoreCoins, setShowMoreCoins] = useState<boolean>(false)
  const router = useRouter()

  async function getTrendingCoins() {
    const response = await fetch("/api/coins/trending", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    })

    const data: TrendingCoinResponse = await response.json()
    return data
  }

  const {
    data: trendingCoins,
    isError,
    isLoading,
  } = useQuery<TrendingCoinResponse>({
    queryKey: ["trending-coins"],
    queryFn: getTrendingCoins,
  })

  if (isLoading) {
    return (
      <View className="gap-3">
        {Array.from({ length: 5 }).map((_, i) => (
          <Animated.View
            key={i}
            entering={FadeInDown.delay(i * 60).duration(400)}
          >
            <View className="flex-row items-center justify-between px-1 py-3 gap-3">
              <View className="flex-row items-center gap-3">
                <Skeleton className="w-5 h-4 rounded" />
                <Skeleton className="w-10 h-10 rounded-full" />
                <Skeleton className="w-16 h-4 rounded" />
              </View>
              <View className="flex-row items-center gap-3">
                <Skeleton className="w-20 h-8 rounded" />
                <Skeleton className="w-14 h-4 rounded" />
                <Skeleton className="w-20 h-4 rounded" />
              </View>
            </View>
          </Animated.View>
        ))}
      </View>
    )
  }

  if (isError) {
    return (
      <View className="items-center justify-center py-12 gap-3">
        <Text className="text-muted-foreground text-sm">
          Não foi possível carregar as tendências.
        </Text>
        <Text className="text-muted-foreground/60 text-xs">
          Tente novamente mais tarde.
        </Text>
      </View>
    )
  }

  const isPositive = (value: number) => value >= 0

  return (
    <View className="gap-1">
      {trendingCoins?.coins
        .slice(0, showMoreCoins ? 15 : 5)
        .map((coin, index) => (
          <Animated.View
            key={coin.id}
            entering={FadeInDown.delay(index * 50)
              .duration(400)
              .springify()
              .damping(18)}
          >
            <Pressable
              onPress={() => router.push("/")}
              className="flex-row items-center justify-between px-2 py-3 rounded-2xl active:bg-muted/50"
            >
              <View className="flex-row items-center gap-2.5">
                <Text className="text-xs text-muted-foreground/70 w-5 text-center">
                  {index + 1}
                </Text>
                <Avatar alt={coin.name} className="w-9 h-9">
                  <AvatarImage source={{ uri: coin.imageUrl }} />
                </Avatar>
                <View>
                  <Text className="font-semibold uppercase tracking-wide text-sm">
                    {coin.symbol}
                  </Text>
                  <Text className="text-xs text-muted-foreground">
                    {coin.name}
                  </Text>
                </View>
              </View>

              <View className="flex-row items-center gap-3">
                <Image
                  source={{ uri: coin.sparkline }}
                  style={sparklineStyle}
                  contentFit="contain"
                  transition={200}
                  cachePolicy="memory-disk"
                />

                <View className="items-end gap-0.5">
                  <Text className="text-sm font-medium">
                    {currencyFormat.format(coin.price)}
                  </Text>
                  <View className="flex-row items-center">
                    {isPositive(coin.priceChangePercentage24h) ? (
                      <ChevronUp size={12} color="#22c55e" />
                    ) : (
                      <ChevronDown size={12} color="#ef4444" />
                    )}
                    <Text
                      className="text-xs font-medium"
                      style={{
                        color: isPositive(coin.priceChangePercentage24h)
                          ? "#22c55e"
                          : "#ef4444",
                      }}
                    >
                      {percentageFormat.format(
                        Math.abs(coin.priceChangePercentage24h)
                      )}
                    </Text>
                  </View>
                </View>
              </View>
            </Pressable>
          </Animated.View>
        ))}

      <Button
        variant="ghost"
        className="mt-1 rounded-xl"
        onPress={() => setShowMoreCoins(!showMoreCoins)}
      >
        <View className="flex-row items-center gap-1.5">
          {showMoreCoins ? (
            <ChevronUp size={16} className="text-muted-foreground" />
          ) : (
            <ChevronDown size={16} className="text-muted-foreground" />
          )}
          <Text className="text-sm text-muted-foreground font-medium">
            {showMoreCoins ? "Mostrar menos" : "Ver todas"}
          </Text>
        </View>
      </Button>
    </View>
  )
}