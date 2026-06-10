import { View } from "react-native"
import { Text } from "@/components/ui/text"
import { CoinDetailResponse } from "@/types/coin-detail-response"
import { Separator } from "@/components/ui/separator"
import Animated, { FadeInDown } from "react-native-reanimated"
import { ChevronDown, ChevronUp, Trophy, ArrowDown } from "lucide-react-native"
import { Icon } from "@/components/ui/icon"

interface CoinDetailAthAtlProps {
  coin: CoinDetailResponse
}

export function CoinDetailAthAtl({ coin }: CoinDetailAthAtlProps) {
  const percentageFormat = new Intl.NumberFormat("pt-BR", {
    style: "percent",
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  })

  const formatDate = (dateStr: string | null | undefined): string => {
    if (!dateStr) return "—"
    try {
      return new Intl.DateTimeFormat("pt-BR", {
        day: "2-digit",
        month: "short",
        year: "numeric",
      }).format(new Date(dateStr))
    } catch {
      return dateStr
    }
  }

  return (
    <Animated.View entering={FadeInDown.delay(400).duration(500)} className="gap-4">
      <View className="flex-row gap-3">
        <View className="flex-1 bg-card border border-border rounded-2xl p-4 gap-2">
          <View className="flex-row items-center gap-2">
            <View className="w-8 h-8 rounded-xl items-center justify-center bg-green-500/10">
              <Icon as={Trophy} className="size-4 text-green-500" />
            </View>
            <Text className="text-xs text-muted-foreground font-medium">ATH</Text>
          </View>
          <Text className="text-lg font-bold">{coin.ath ?? "—"}</Text>
          {coin.athChangePercentage != null && (
            <View className="flex-row items-center gap-0.5">
              <ChevronDown size={12} color="#ef4444" />
              <Text className="text-xs font-medium" style={{ color: "#ef4444" }}>
                {percentageFormat.format(Math.abs(coin.athChangePercentage))}
              </Text>
            </View>
          )}
          <Text className="text-[10px] text-muted-foreground/60">{formatDate(coin.athDate)}</Text>
        </View>

        <View className="flex-1 bg-card border border-border rounded-2xl p-4 gap-2">
          <View className="flex-row items-center gap-2">
            <View className="w-8 h-8 rounded-xl items-center justify-center bg-red-500/10">
              <Icon as={ArrowDown} className="size-4 text-red-500" />
            </View>
            <Text className="text-xs text-muted-foreground font-medium">ATL</Text>
          </View>
          <Text className="text-lg font-bold">{coin.atl ?? "—"}</Text>
          {coin.atlChangePercentage != null && (
            <View className="flex-row items-center gap-0.5">
              <ChevronUp size={12} color="#22c55e" />
              <Text className="text-xs font-medium" style={{ color: "#22c55e" }}>
                {percentageFormat.format(Math.abs(coin.atlChangePercentage))}
              </Text>
            </View>
          )}
          <Text className="text-[10px] text-muted-foreground/60">{formatDate(coin.atlDate)}</Text>
        </View>
      </View>
    </Animated.View>
  )
}
