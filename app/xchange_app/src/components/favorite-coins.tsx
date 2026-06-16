import { Pressable, ScrollView, View } from "react-native";
import { useEffect, useState } from "react";
import { useQuery } from "@tanstack/react-query";
import { apiFetch } from "@/lib/api-fetch";
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "./ui/card";
import { Avatar, AvatarFallback, AvatarImage } from "./ui/avatar";
import { Text } from "./ui/text";
import { Skeleton } from "./ui/skeleton";
import { cn } from "@/lib/utils";
import { HelpCircle, Minus, Star, TrendingDown, TrendingUp } from "lucide-react-native";
import { FavoriteCoinsWithPredictionResponse } from "@/types/favorite-coins-with-prediction-response";
import { Icon } from "./ui/icon";
import { Button } from "./ui/button";
import { Link } from "expo-router";
import { CoinDetailResponse } from "@/types/coin-detail-response";
import { Separator } from "./ui/separator";
import { compactUsdFromFormatted } from "@/utils/currency-format";

const PREDICTION_PROPS = [
  {
    id: "lateral",
    name: "Lateral",
    description: "Estabilidade no preço.",
    recomendation: "Recomendação neutra.",
    hexColor: "#fcf403",
    icon: Minus
  },
  {
    id: "alta",
    name: "Alta",
    description: "Potencial de valorização.",
    recomendation: "Compra recomendada.",
    hexColor: "#43eb34",
    icon: TrendingUp
  },
  {
    id: "baixa",
    name: "Baixa",
    description: "Possível desvalorização.",
    recomendation: "Venda recomendada.",
    hexColor: "#eb3a34",
    icon: TrendingDown
  },
  {
    id: "sem_dados",
    name: "Sem dados",
    description: "Dados insuficientes para análise.",
    recomendation: "Não há recomendação.",
    hexColor: "#a1a1aa",
    icon: HelpCircle
  }
]

const DEFAULT_PREDICTION = PREDICTION_PROPS.find(p => p.id === "sem_dados")!

export function FavoriteCoins() {
  const [selectedCoinId, setSelectedCoinId] = useState<string>("");

  const { data: favoriteCoins = [], isLoading } = useQuery<FavoriteCoinsWithPredictionResponse>({
    queryKey: ["favorite-coins"],
    queryFn: async () => {
      const response = await apiFetch({
        input: `/api/profile/favorite-coins?prediction=${true}`,
        init: {
          method: "GET"
        }
      })

      if (!response.ok) return []

      const data = await response.json()
      return data
    }
  })

  const { data: coinDetail, isLoading: isLoadingDetail } = useQuery<CoinDetailResponse>({
    queryKey: ["coin-detail-favorite", selectedCoinId],
    queryFn: async () => {
      const response = await apiFetch({
        input: `/api/coins/details/${selectedCoinId}`,
        init: { method: "GET" }
      })
      return await response.json()
    },
    enabled: !!selectedCoinId
  })

  useEffect(() => {
    if (favoriteCoins.length > 0 && !selectedCoinId) {
      setSelectedCoinId(favoriteCoins[0].coinId)
    }
  }, [favoriteCoins])

  const selectedCoin = favoriteCoins.find(c => c.coinId === selectedCoinId)
  const predictionProps = selectedCoin
    ? (PREDICTION_PROPS.find(value => selectedCoin.prediction === value.id) ?? DEFAULT_PREDICTION)
    : undefined

  if (isLoading) {
    return (
      <ScrollView
        horizontal
        showsHorizontalScrollIndicator={false}
        contentContainerClassName="gap-3"
      >
        {Array.from({ length: 3 }).map((_, i) => (
          <Card key={i} className="w-44">
            <CardHeader className="flex-row items-center gap-2">
              <Skeleton className="w-8 h-8 rounded-full" />
              <View className="gap-1">
                <Skeleton className="w-12 h-3 rounded" />
                <Skeleton className="w-20 h-3 rounded" />
              </View>
            </CardHeader>
            <CardContent>
              <View className="flex-row items-center gap-1.5">
                <Skeleton className="w-3 h-3 rounded-full" />
                <Skeleton className="w-16 h-3 rounded" />
              </View>
            </CardContent>
          </Card>
        ))}
      </ScrollView>
    )
  }

  if (favoriteCoins.length === 0) {
    return (
      <View className="items-center justify-center py-6 gap-3">
        <Icon as={Star} size={32} className="text-muted-foreground" />
        <Text className="text-muted-foreground text-sm">
          Nenhuma cripto favoritada ainda.
        </Text>
        <Text className="text-muted-foreground/60 text-xs">
          Favorite moedas para acompanhá-las aqui.
        </Text>
      </View>
    )
  }

  return (
    <View className="flex flex-col gap-2">
      <ScrollView
        horizontal
        showsHorizontalScrollIndicator={false}
        contentContainerClassName="gap-3"
      >
        {favoriteCoins.map((coin) => {
          const isSelected = selectedCoinId === coin.coinId
          const coinPrediction = PREDICTION_PROPS.find(value => coin.prediction === value.id) ?? DEFAULT_PREDICTION

          return (
            <Pressable
              key={coin.coinId}
              onPress={() => setSelectedCoinId(coin.coinId)}
            >
              <Card className={cn(
                "w-44",
                isSelected && "border-primary"
              )}>
                <CardHeader className="flex-row items-center gap-2">
                  <Avatar alt={coin.name} className="w-8 h-8">
                    {coin.imageUrl ? (
                      <AvatarImage source={{ uri: coin.imageUrl }} />
                    ) : (
                      <AvatarFallback>
                        <Text className="text-xs">{coin.symbol.slice(0, 2).toUpperCase()}</Text>
                      </AvatarFallback>
                    )}
                  </Avatar>

                  <View>
                    <CardTitle className="text-sm uppercase">{coin.symbol}</CardTitle>
                    <CardDescription className="text-xs">{coin.name}</CardDescription>
                  </View>
                </CardHeader>

                <CardContent>
                  <View className="flex flex-row self-start items-center gap-1 border px-2 py-0.5 rounded-sm" style={{borderColor: coinPrediction?.hexColor}}>
                    <Icon as={coinPrediction?.icon!} className="w-4 h-4" color={coinPrediction?.hexColor} />
                    <Text className="text-sm font-semibold" style={{color: coinPrediction?.hexColor}}>
                      {coinPrediction?.name}
                    </Text>
                  </View>
                </CardContent>
              </Card>
            </Pressable>
          )
        })}
      </ScrollView>

      {isLoadingDetail ? (
        <Card>
          <CardHeader>
            <Skeleton className="w-32 h-5 rounded" />
          </CardHeader>
          <CardContent className="gap-2">
            <Skeleton className="w-full h-4 rounded" />
            <Skeleton className="w-48 h-4 rounded" />
            <Skeleton className="w-24 h-4 rounded" />
          </CardContent>
        </Card>
      ) : coinDetail && selectedCoin ? (
        <Card>
          <CardHeader>
            <View className="flex-row items-center gap-2">
              <Avatar alt={coinDetail.name} className="w-8 h-8">
                {coinDetail.imageUrl ? (
                  <AvatarImage source={{ uri: coinDetail.imageUrl }} />
                ) : (
                  <AvatarFallback>
                    <Text className="text-xs">{coinDetail.symbol?.slice(0, 2).toUpperCase()}</Text>
                  </AvatarFallback>
                )}
              </Avatar>
              <View>
                <CardTitle className="text-base">{coinDetail.name}</CardTitle>
                <CardDescription className="text-xs uppercase">{coinDetail.symbol}</CardDescription>
              </View>
            </View>
          </CardHeader>
          <CardContent className="gap-2">
            {predictionProps && (
              <View className="flex-row items-center gap-1.5 mb-1">
                <Icon as={predictionProps.icon} className="w-4 h-4" color={predictionProps.hexColor} />
                <View>
                  <Text className="text-lg font-semibold" style={{ color: predictionProps.hexColor }}>
                    {predictionProps.description}
                  </Text>
                  <Text className="text-sm" style={{ color: predictionProps.hexColor }}>
                    {predictionProps.recomendation}
                  </Text>
                </View>
              </View>
            )}

            <Separator />

            <View className="flex-row justify-between">
              <Text className="text-xs text-muted-foreground">Preço</Text>
              <Text className="text-xs font-semibold">{coinDetail.price ?? "—"}</Text>
            </View>
            <View className="flex-row justify-between">
              <Text className="text-xs text-muted-foreground">Market Cap</Text>
              <Text className="text-xs font-semibold">{compactUsdFromFormatted(coinDetail.marketCap)}</Text>
            </View>
            {coinDetail.priceChangePercentage24h != null && (
              <View className="flex-row justify-between">
                <Text className="text-xs text-muted-foreground">Variação 24h</Text>
                <Text
                  className={cn(
                    "text-xs font-semibold",
                    coinDetail.priceChangePercentage24h >= 0 ? "text-emerald-400" : "text-red-400"
                  )}
                >
                  {coinDetail.priceChangePercentage24h.toFixed(2)}%
                </Text>
              </View>
            )}
          </CardContent>
          <CardFooter>
            <Link href={`/coin/${coinDetail.id}`} asChild>
              <Button className="w-full" variant={"outline"}>
                <Text>Acessar informações da moeda</Text>
              </Button>
            </Link>
          </CardFooter>
        </Card>
      ) : null}
    </View>
  )
}