import { View } from "react-native"
import { Text } from "@/components/ui/text"
import { CoinDetailResponse } from "@/types/coin-detail-response"
import { Separator } from "@/components/ui/separator"
import Animated, { FadeInDown } from "react-native-reanimated"
import {
  BarChart3,
  Activity,
  ArrowUpDown,
  TrendingUp,
  TrendingDown,
  DollarSign,
  Layers,
} from "lucide-react-native"
import { Icon } from "@/components/ui/icon"
import { type LucideIcon } from "lucide-react-native"
import { compactUsdFromFormatted } from "@/utils/currency-format"

interface CoinDetailMarketDataProps {
  coin: CoinDetailResponse
}

interface StatRowProps {
  icon: LucideIcon
  iconColor: string
  label: string
  value: string | null | undefined
}

function StatRow({ icon, iconColor, label, value }: StatRowProps) {
  return (
    <View className="flex-row items-center justify-between py-3">
      <View className="flex-row items-center gap-2.5 shrink-0">
        <View className="w-8 h-8 rounded-xl items-center justify-center" style={{ backgroundColor: iconColor + "18" }}>
          <Icon as={icon} className="size-4" color={iconColor} />
        </View>
        <Text className="text-sm text-muted-foreground">{label}</Text>
      </View>
      <Text
        className="text-sm font-semibold text-right flex-1 ml-3"
        numberOfLines={1}
        adjustsFontSizeToFit
        minimumFontScale={0.7}
      >
        {value ?? "—"}
      </Text>
    </View>
  )
}

function formatLargeNumber(value: number | null | undefined): string {
  if (value == null) return "—"
  const formatter = new Intl.NumberFormat("en-US", {
    notation: "compact",
    maximumFractionDigits: 2,
  })
  return formatter.format(value)
}

export function CoinDetailMarketData({ coin }: CoinDetailMarketDataProps) {
  return (
    <Animated.View entering={FadeInDown.delay(300).duration(500)} className="gap-4">
      <View className="bg-card border border-border rounded-2xl px-4">
        <StatRow icon={DollarSign} iconColor="#f59e0b" label="Market Cap" value={compactUsdFromFormatted(coin.marketCap)} />
        <Separator />
        <StatRow icon={Activity} iconColor="#3b82f6" label="Volume 24h" value={compactUsdFromFormatted(coin.totalVolume)} />
        <Separator />
        <StatRow icon={Layers} iconColor="#8b5cf6" label="FDV" value={compactUsdFromFormatted(coin.fullyDilutedValuation)} />
      </View>

      <View className="bg-card border border-border rounded-2xl px-4">
        <StatRow icon={TrendingUp} iconColor="#22c55e" label="Máxima 24h" value={coin.high24h} />
        <Separator />
        <StatRow icon={TrendingDown} iconColor="#ef4444" label="Mínima 24h" value={coin.low24h} />
        <Separator />
        <StatRow icon={ArrowUpDown} iconColor="#f59e0b" label="Variação 24h" value={coin.priceChange24h} />
      </View>

      <View className="bg-card border border-border rounded-2xl px-4">
        <StatRow icon={BarChart3} iconColor="#22c55e" label="Suprimento Circulante" value={formatLargeNumber(coin.circulatingSupply)} />
        <Separator />
        <StatRow icon={BarChart3} iconColor="#3b82f6" label="Suprimento Total" value={formatLargeNumber(coin.totalSupply)} />
        <Separator />
        <StatRow icon={BarChart3} iconColor="#8b5cf6" label="Suprimento Máximo" value={formatLargeNumber(coin.maxSupply)} />
      </View>
    </Animated.View>
  )
}
