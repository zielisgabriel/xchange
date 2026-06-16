import { View, ScrollView } from "react-native"
import { Text } from "@/components/ui/text"
import { CoinDetailResponse } from "@/types/coin-detail-response"
import { Separator } from "@/components/ui/separator"
import Animated, { FadeInDown } from "react-native-reanimated"
import { Info, Calendar, Hash } from "lucide-react-native"
import { Icon } from "@/components/ui/icon"

interface CoinDetailInfoProps {
  coin: CoinDetailResponse
}

export function CoinDetailInfo({ coin }: CoinDetailInfoProps) {
  const formatDate = (dateStr: string | null | undefined): string => {
    if (!dateStr) return "—"
    try {
      return new Intl.DateTimeFormat("pt-BR", {
        day: "2-digit",
        month: "long",
        year: "numeric",
      }).format(new Date(dateStr))
    } catch {
      return dateStr
    }
  }

  const hasInfo = coin.hashingAlgorithm || coin.genesisDate || coin.description

  if (!hasInfo) return null

  return (
    <Animated.View entering={FadeInDown.delay(600).duration(500)} className="bg-card border border-border rounded-2xl px-4 gap-0">
      {coin.hashingAlgorithm && (
        <>
          <View className="flex-row items-center justify-between py-3">
            <View className="flex-row items-center gap-2.5">
              <View className="w-8 h-8 rounded-xl items-center justify-center bg-purple-500/10">
                <Icon as={Hash} className="size-4 text-purple-500" />
              </View>
              <Text className="text-sm text-muted-foreground">Algoritmo</Text>
            </View>
            <Text className="text-sm font-semibold text-right flex-1 ml-3" numberOfLines={1}>{coin.hashingAlgorithm}</Text>
          </View>
          {coin.genesisDate && <Separator />}
        </>
      )}

      {coin.genesisDate && (
        <>
          <View className="flex-row items-center justify-between py-3">
            <View className="flex-row items-center gap-2.5">
              <View className="w-8 h-8 rounded-xl items-center justify-center bg-amber-500/10">
                <Icon as={Calendar} className="size-4 text-amber-500" />
              </View>
              <Text className="text-sm text-muted-foreground">Lançamento</Text>
            </View>
            <Text className="text-sm font-semibold">{formatDate(coin.genesisDate)}</Text>
          </View>
          {coin.description && <Separator />}
        </>
      )}

      {coin.description && (
        <View className="py-3 gap-2">
          <View className="flex-row items-center gap-2.5">
            <View className="w-8 h-8 rounded-xl items-center justify-center bg-blue-500/10">
              <Icon as={Info} className="size-4 text-blue-500" />
            </View>
            <Text className="text-sm text-muted-foreground">Sobre</Text>
          </View>
          <ScrollView 
            nestedScrollEnabled 
            className="max-h-32 pl-[42px]"
            showsVerticalScrollIndicator={true}
            indicatorStyle="white"
          >
            <Text className="text-xs text-muted-foreground/80 leading-5 pr-2 pb-2">
              {coin.description.replace(/<[^>]*>/g, "")}
            </Text>
          </ScrollView>
        </View>
      )}
    </Animated.View>
  )
}
