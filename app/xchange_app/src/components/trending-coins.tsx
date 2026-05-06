import { CoinWithMarketData } from "@/types/coin-with-market-data"
import { Text } from "./ui/text"
import { useQuery } from "@tanstack/react-query"
import { View } from "react-native"
import { Button } from "./ui/button"
import { ScrollView } from "react-native-gesture-handler"
import { Avatar, AvatarImage } from "./ui/avatar"
import { Separator } from "./ui/separator"
import { Image } from "expo-image"
import { useState } from "react"
import { ChevronDown, ChevronUp } from "lucide-react-native"
import clsx from "clsx"
import { twMerge } from "tailwind-merge"

interface TrendingCoinResponse {
  coins: CoinWithMarketData[]
}

export function TrendingCoins() {
  const [showMoreCoins, setShowMoreCoins] = useState<boolean>(false);

  async function getTrendingCoins() {
    const response = await fetch("/api/coins/trending", {
      method: "GET",
      headers: {
        "Content-Type": "application/json"
      }
    })

    const data: TrendingCoinResponse = await response.json()
    return data
  }

  const {
    data: trendingCoins,
    isError
  } = useQuery<TrendingCoinResponse>({ queryKey: ['trending-coins'], queryFn: getTrendingCoins })

  const currencyFormat = Intl.NumberFormat("en-US", {
      currency: "USD",
      style: "currency",
      maximumFractionDigits: 5,
    })

  return (
    <ScrollView>
        <View className="flex">
          {trendingCoins?.coins.slice(0, showMoreCoins ? 15 : 5).map((coin, index) => (
            <View key={coin.id}>
              <Button
                className="text-white h-16 py-0 px-4 justify-between rounded-none"
                variant={"ghost"}
                size={"lg"}
              >
                <View>
                  <View className="flex flex-row gap-2 items-center">
                    <Text className="text-muted-foreground">
                      # {index + 1}
                    </Text>
                    <Avatar alt={coin.name}>
                      <AvatarImage source={{ uri: coin.imageUrl }} />
                    </Avatar>
                    <View>
                      <Text className="font-black tracking-wider">
                        {coin.name}
                      </Text>
                      <Text className="text-muted-foreground uppercase">
                        {coin.symbol}
                      </Text>
                    </View>
                  </View>
                </View>

                <View className="flex flex-row gap-2 justify-end">
                  <Image source={coin.sparkline} className="w-full object-cover" />

                  <Text>
                    {currencyFormat.format(coin.price)}
                  </Text>
                </View>
              </Button>
              <Separator />
            </View>
          ))}

          <Button variant={"link"} onPress={() => setShowMoreCoins(!showMoreCoins)}>
            <View>
              <Text className="flex flex-row items-center gap-1 text-muted-foreground">
                {showMoreCoins ? (
                  <ChevronUp />
                ) : (
                  <ChevronDown />
                )}
                Mostrar {showMoreCoins ? "menos" : "mais"}
              </Text>
            </View>
          </Button>
      </View>
    </ScrollView>
  )
}