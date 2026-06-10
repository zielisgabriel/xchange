import { View } from "react-native"
import { Text } from "@/components/ui/text"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import Animated, { FadeIn, FadeInDown } from "react-native-reanimated"
import { CoinDetailResponse } from "@/types/coin-detail-response"
import { ChevronDown, ChevronUp } from "lucide-react-native"

interface CoinDetailHeaderProps {
  coin: CoinDetailResponse
}

export function CoinDetailHeader({ coin }: CoinDetailHeaderProps) {
  const isPositive = coin.priceChangePercentage24h >= 0
  const percentageFormat = new Intl.NumberFormat("pt-BR", {
    style: "percent",
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  })

  return (
    <Animated.View entering={FadeIn.duration(500)} className="items-center gap-3 pt-2 pb-4">
      <Avatar alt={coin.name} className="w-16 h-16">
        {coin.imageUrl ? (
          <AvatarImage source={{ uri: coin.imageUrl }} />
        ) : (
          <AvatarFallback>
            <Text className="text-lg font-bold">{coin.symbol?.slice(0, 2).toUpperCase() ?? "?"}</Text>
          </AvatarFallback>
        )}
      </Avatar>

      <View className="items-center gap-0.5">
        <Text className="text-xl font-bold tracking-tight">{coin.name ?? ""}</Text>
        <Text className="text-sm text-muted-foreground uppercase">{coin.symbol ?? ""}</Text>
      </View>

      <Animated.View entering={FadeInDown.delay(100).duration(400)} className="items-center gap-1">
        <Text className="text-3xl font-extrabold tracking-tight">{coin.price}</Text>
        <View className="flex-row items-center gap-1 px-3 py-1 rounded-full" style={{ backgroundColor: isPositive ? "rgba(34,197,94,0.12)" : "rgba(239,68,68,0.12)" }}>
          {isPositive ? (
            <ChevronUp size={14} color="#22c55e" />
          ) : (
            <ChevronDown size={14} color="#ef4444" />
          )}
          <Text className="text-sm font-semibold" style={{ color: isPositive ? "#22c55e" : "#ef4444" }}>
            {percentageFormat.format(Math.abs(coin.priceChangePercentage24h))}
          </Text>
          <Text className="text-xs text-muted-foreground ml-0.5">24h</Text>
        </View>
      </Animated.View>

      {coin.marketCapRank && (
        <Animated.View entering={FadeInDown.delay(200).duration(400)} className="flex-row items-center gap-1.5 bg-primary/10 px-3 py-1 rounded-full">
          <Text className="text-xs text-primary font-semibold">Rank #{coin.marketCapRank}</Text>
        </Animated.View>
      )}
    </Animated.View>
  )
}
