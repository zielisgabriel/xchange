import { View } from "react-native"
import { Text } from "@/components/ui/text"
import { CoinDetailResponse } from "@/types/coin-detail-response"
import Animated, { FadeInDown } from "react-native-reanimated"
import Svg, { Polyline, Defs, LinearGradient as SvgLinearGradient, Stop, Rect } from "react-native-svg"

interface CoinDetailSparklineProps {
  coin: CoinDetailResponse
}

const CHART_WIDTH = 340
const CHART_HEIGHT = 140

function buildPolylinePoints(data: number[]): string {
  if (!data || data.length === 0) return ""

  const min = Math.min(...data)
  const max = Math.max(...data)
  const range = max - min || 1

  return data
    .map((value, index) => {
      const x = (index / (data.length - 1)) * CHART_WIDTH
      const y = CHART_HEIGHT - ((value - min) / range) * (CHART_HEIGHT - 8) - 4
      return `${x},${y}`
    })
    .join(" ")
}

export function CoinDetailSparkline({ coin }: CoinDetailSparklineProps) {
  if (!coin.sparkline7d || coin.sparkline7d.length === 0) return null

  const isPositive = coin.sparkline7d[coin.sparkline7d.length - 1] >= coin.sparkline7d[0]
  const strokeColor = isPositive ? "#22c55e" : "#ef4444"
  const points = buildPolylinePoints(coin.sparkline7d)

  const timeframes = [
    { label: "1h", value: coin.priceChangePercentage1h },
    { label: "24h", value: coin.priceChangePercentage24h },
    { label: "7d", value: coin.priceChangePercentage7d },
    { label: "30d", value: coin.priceChangePercentage30d },
  ]

  const percentageFormat = new Intl.NumberFormat("pt-BR", {
    style: "percent",
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  })

  return (
    <Animated.View entering={FadeInDown.delay(200).duration(500)} className="gap-4">
      <View className="bg-card border border-border rounded-2xl p-4 overflow-hidden">
        <Text className="text-xs text-muted-foreground font-medium mb-3">Últimos 7 dias</Text>
        <Svg width="100%" height={CHART_HEIGHT} viewBox={`0 0 ${CHART_WIDTH} ${CHART_HEIGHT}`} preserveAspectRatio="none">
          <Defs>
            <SvgLinearGradient id="sparkGrad" x1="0" y1="0" x2="0" y2="1">
              <Stop offset="0" stopColor={strokeColor} stopOpacity="0.15" />
              <Stop offset="1" stopColor={strokeColor} stopOpacity="0" />
            </SvgLinearGradient>
          </Defs>
          <Rect x="0" y="0" width={CHART_WIDTH} height={CHART_HEIGHT} fill="url(#sparkGrad)" />
          <Polyline
            points={points}
            fill="none"
            stroke={strokeColor}
            strokeWidth="2"
            strokeLinejoin="round"
            strokeLinecap="round"
          />
        </Svg>
      </View>

      <View className="flex-row gap-2">
        {timeframes.map((tf) => {
          const positive = tf.value != null && tf.value >= 0
          return (
            <View key={tf.label} className="flex-1 bg-card border border-border rounded-xl py-3 items-center gap-1">
              <Text className="text-[10px] text-muted-foreground font-medium uppercase">{tf.label}</Text>
              <Text
                className="text-sm font-bold"
                style={{ color: tf.value == null ? "#a1a1aa" : positive ? "#22c55e" : "#ef4444" }}
              >
                {tf.value != null ? percentageFormat.format(Math.abs(tf.value)) : "—"}
              </Text>
            </View>
          )
        })}
      </View>
    </Animated.View>
  )
}
