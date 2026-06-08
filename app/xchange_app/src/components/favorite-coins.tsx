import { Pressable, ScrollView, View } from "react-native";
import { useEffect, useState } from "react";
import { useQuery } from "@tanstack/react-query";
import { apiFetch } from "@/lib/api-fetch";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "./ui/card";
import { Avatar, AvatarFallback, AvatarImage } from "./ui/avatar";
import { Text } from "./ui/text";
import { Skeleton } from "./ui/skeleton";
import { Minus, Star, TrendingDown, TrendingUp } from "lucide-react-native";
import { cn } from "@/lib/utils";
import { FavoriteCoinChart } from "./favorite-coin-chart";

type CoinStatus = "up" | "stable" | "down" | "no_data"

interface FavoriteCoin {
  id: string,
  name: string,
  symbol: string,
  imageUrl?: string,
  status: CoinStatus,
}

const statusConfig: Record<CoinStatus, { label: string, color: string, icon: typeof TrendingUp }> = {
  up: { label: "Em alta", color: "#22c55e", icon: TrendingUp },
  stable: { label: "Estável", color: "#eab308", icon: Minus },
  down: { label: "Em baixa", color: "#ef4444", icon: TrendingDown },
  no_data: { label: "Sem dados de IA", color: "#9ca3af", icon: Minus },
}

export function FavoriteCoins() {
  const [selectedCoin, setSelectedCoin] = useState<string>("");

  const { data: favorites = [], isLoading } = useQuery({
    queryKey: ["favoritesStatus"],
    queryFn: async () => {
      const response = await apiFetch({
        input: "/api/favorites/status",
        init: {
          method: "GET"
        }
      })

      if (!response.ok) {
        return []
      }

      const data = await response.json()
      return data as FavoriteCoin[]
    }
  })

  useEffect(() => {
    if (favorites.length > 0 && !selectedCoin) {
      setSelectedCoin(favorites[0].id)
    }
  }, [favorites])

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

  if (favorites.length === 0) {
    return (
      <View className="items-center justify-center py-6 gap-3">
        <Star size={32} className="text-muted-foreground/40" />
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
    <View>
      <ScrollView
        horizontal
        showsHorizontalScrollIndicator={false}
        contentContainerClassName="gap-3"
      >
        {favorites.map((coin) => {
          const isSelected = selectedCoin === coin.id
          const { label, color, icon: StatusIcon } = statusConfig[coin.status]

          return (
            <Pressable
              key={coin.id}
              onPress={() => setSelectedCoin(coin.id)}
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
                        <Text className="text-xs">{coin.symbol.slice(0, 2)}</Text>
                      </AvatarFallback>
                    )}
                  </Avatar>

                  <View>
                    <CardTitle className="text-sm uppercase">{coin.symbol}</CardTitle>
                    <CardDescription className="text-xs">{coin.name}</CardDescription>
                  </View>
                </CardHeader>

                <CardContent>
                  <View className="flex-row items-center gap-1.5">
                    <StatusIcon size={14} color={color} />
                    <Text className="text-sm font-medium" style={{ color }}>
                      {label}
                    </Text>
                  </View>
                </CardContent>
              </Card>
            </Pressable>
          )
        })}
      </ScrollView>

      <FavoriteCoinChart coinId={selectedCoin} />
    </View>
  )
}