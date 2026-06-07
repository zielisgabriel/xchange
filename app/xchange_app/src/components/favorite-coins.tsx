import { Pressable, ScrollView, View } from "react-native";
import { useEffect, useState } from "react";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "./ui/card";
import { Avatar, AvatarFallback, AvatarImage } from "./ui/avatar";
import { Text } from "./ui/text";
import { Minus, Star, TrendingDown, TrendingUp } from "lucide-react-native";
import { cn } from "@/lib/utils";

type CoinStatus = "up" | "stable" | "down"

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
}

// TODO: substituir por dados reais da API
const mockFavorites: FavoriteCoin[] = [
  { id: "bitcoin", name: "Bitcoin", symbol: "BTC", status: "up" },
  { id: "ethereum", name: "Ethereum", symbol: "ETH", status: "stable" },
  { id: "solana", name: "Solana", symbol: "SOL", status: "down" },
]

export function FavoriteCoins() {
  const favorites = mockFavorites
  const [selectedCoin, setSelectedCoin] = useState<string>("");

  useEffect(() => {
    if (favorites.length > 0 && !selectedCoin) {
      setSelectedCoin(favorites[0].id)
    }
  }, [favorites])

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
                  <CardTitle className="text-sm">{coin.symbol}</CardTitle>
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
  )
}