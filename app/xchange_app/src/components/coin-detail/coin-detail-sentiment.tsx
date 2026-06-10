import { View } from "react-native"
import { Text } from "@/components/ui/text"
import { CoinDetailResponse } from "@/types/coin-detail-response"
import Animated, { FadeInDown } from "react-native-reanimated"
import { ThumbsUp, ThumbsDown, Users } from "lucide-react-native"
import { Icon } from "@/components/ui/icon"

interface CoinDetailSentimentProps {
  coin: CoinDetailResponse
}

export function CoinDetailSentiment({ coin }: CoinDetailSentimentProps) {
  const upPct = coin.sentimentVotesUpPercentage ?? 0
  const downPct = coin.sentimentVotesDownPercentage ?? 0
  const total = upPct + downPct
  const upWidth = total > 0 ? (upPct / total) * 100 : 50

  const numberFormat = new Intl.NumberFormat("en-US", {
    notation: "compact",
    maximumFractionDigits: 1,
  })

  return (
    <Animated.View entering={FadeInDown.delay(500).duration(500)} className="bg-card border border-border rounded-2xl p-4 gap-4">
      <View className="flex-row items-center gap-2">
        <View className="w-8 h-8 rounded-xl items-center justify-center bg-blue-500/10">
          <Icon as={Users} className="size-4 text-blue-500" />
        </View>
        <Text className="text-sm font-semibold">Sentimento da Comunidade</Text>
      </View>

      <View className="gap-2">
        <View className="flex-row h-3 rounded-full overflow-hidden bg-muted">
          <View className="h-full rounded-l-full" style={{ width: `${upWidth}%`, backgroundColor: "#22c55e" }} />
          <View className="h-full rounded-r-full flex-1" style={{ backgroundColor: "#ef4444" }} />
        </View>

        <View className="flex-row items-center justify-between">
          <View className="flex-row items-center gap-1.5">
            <ThumbsUp size={12} color="#22c55e" />
            <Text className="text-xs font-medium" style={{ color: "#22c55e" }}>
              {upPct.toFixed(1)}%
            </Text>
          </View>
          <View className="flex-row items-center gap-1.5">
            <ThumbsDown size={12} color="#ef4444" />
            <Text className="text-xs font-medium" style={{ color: "#ef4444" }}>
              {downPct.toFixed(1)}%
            </Text>
          </View>
        </View>
      </View>

      {coin.watchlistPortfolioUsers != null && (
        <View className="flex-row items-center gap-2 pt-1">
          <Text className="text-xs text-muted-foreground">
            {numberFormat.format(coin.watchlistPortfolioUsers)} usuários acompanhando
          </Text>
        </View>
      )}
    </Animated.View>
  )
}
