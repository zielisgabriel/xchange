import { CoinWithMarketData } from "@/types/coin-with-market-data"
import { apiFetch } from "@/lib/api-fetch"
import { Text } from "./ui/text"
import { useQuery } from "@tanstack/react-query"
import { View } from "react-native"
import { Button } from "./ui/button"
import { useState } from "react"
import { ChevronDown, ChevronUp } from "lucide-react-native"
import { Skeleton } from "./ui/skeleton"
import Animated, { FadeInDown } from "react-native-reanimated"
import { CoinItem } from "./coin-item"
import { Icon } from "./ui/icon"

interface TrendingCoinResponse {
  coins: CoinWithMarketData[]
}

export function TrendingCoins() {
  const [showMoreCoins, setShowMoreCoins] = useState<boolean>(false)

  async function getTrendingCoins() {
    const response = await apiFetch({
      input: "/api/coins/trending",
      init: {
        method: "GET"
      }
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

  return (
    <View className="gap-1">
      {trendingCoins?.coins && trendingCoins?.coins.length > 0 && trendingCoins?.coins
        .slice(0, showMoreCoins ? 15 : 5)
        .map((coin, index) => (
          <CoinItem.Root key={coin.id} coinId={coin.id} index={index} routeUrl={`/coin/${coin.id}`}>
            <CoinItem.Content>
              <CoinItem.Rank>
                {index + 1}
              </CoinItem.Rank>
              <CoinItem.Logo name={coin.id} imageUrl={coin.imageUrl} />

              <CoinItem.TitleContent>
                <CoinItem.Symbol>
                  {coin.symbol}
                </CoinItem.Symbol>
                <CoinItem.Name>
                  {coin.name}
                </CoinItem.Name>
              </CoinItem.TitleContent>
            </CoinItem.Content>

            <CoinItem.MarketContent>
              <CoinItem.Sparkline sparklineUri={coin.sparkline} />

              <CoinItem.MarketData>
                <CoinItem.Price>
                  {coin.price}
                </CoinItem.Price>
                <CoinItem.Percentage>
                  {coin.priceChangePercentage24h}
                </CoinItem.Percentage>
              </CoinItem.MarketData>
            </CoinItem.MarketContent>
          </CoinItem.Root>
        ))}

      <Button
        variant="ghost"
        className="mt-1 rounded-xl"
        onPress={() => setShowMoreCoins(!showMoreCoins)}
      >
        <View className="flex-row items-center gap-1.5">
          {showMoreCoins ? (
            <Icon as={ChevronUp} size={16} className="text-muted-foreground" />
          ) : (
            <Icon as={ChevronDown} size={16} className="text-muted-foreground" />
          )}
          <Text className="text-sm text-muted-foreground font-medium">
            {showMoreCoins ? "Mostrar menos" : "Ver todas"}
          </Text>
        </View>
      </Button>
    </View>
  )
}