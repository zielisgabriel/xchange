import { CoinWithMarketData } from "@/types/coin-with-market-data"
import { Text } from "./ui/text"
import { useQuery } from "@tanstack/react-query"
import { View } from "react-native"
import { Button } from "./ui/button"
import { ScrollView } from "react-native-gesture-handler"
import { Avatar, AvatarImage } from "./ui/avatar"
import { Separator } from "./ui/separator"

interface TrendingCoinResponse {
  coins: CoinWithMarketData[]
}

export function TrendingCoins() {
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

  return (
    <ScrollView>
        <View className="flex">
          {trendingCoins?.coins.map(coin => (
            <>
              <Button
                key={coin.id}
                className="text-white h-16 py-0 px-4 justify-between rounded-none"
                variant={"ghost"}
                size={"lg"}
              >
                <View>
                  <View className="flex flex-row gap-2 items-center">
                    <Avatar alt={coin.name}>
                      <AvatarImage source={{ uri: coin.imageUrl }} />
                    </Avatar>
                    <View>
                      <Text className="font-black tracking-wider">
                        {coin.name}
                      </Text>
                      <Text className="text-muted-foreground">
                        {coin.symbol}
                      </Text>
                    </View>
                  </View>
                </View>

                <View>
                  <Text>
                    {coin.price}
                  </Text>
                </View>

              </Button>
              
              <Separator />
            </>
          ))}
      </View>
    </ScrollView>
  )
}