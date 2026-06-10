import { View, ScrollView, RefreshControl } from "react-native"
import { useLocalSearchParams, Stack } from "expo-router"
import { useQuery, useQueryClient } from "@tanstack/react-query"
import { apiFetch } from "@/lib/api-fetch"
import { CoinDetailResponse } from "@/types/coin-detail-response"
import { Text } from "@/components/ui/text"
import { CoinDetailHeader } from "@/components/coin-detail/coin-detail-header"
import { CoinDetailSparkline } from "@/components/coin-detail/coin-detail-sparkline"
import { CoinDetailMarketData } from "@/components/coin-detail/coin-detail-market-data"
import { CoinDetailAthAtl } from "@/components/coin-detail/coin-detail-ath-atl"
import { CoinDetailSentiment } from "@/components/coin-detail/coin-detail-sentiment"
import { CoinDetailInfo } from "@/components/coin-detail/coin-detail-info"
import { CoinDetailSkeleton } from "@/components/coin-detail/coin-detail-skeleton"
import { useCallback, useState } from "react"
import Animated, { FadeIn } from "react-native-reanimated"

export default function CoinPage() {
  const { coinId } = useLocalSearchParams<{ coinId: string }>()
  const queryClient = useQueryClient()
  const [refreshing, setRefreshing] = useState(false)

  const {
    data: coin,
    isLoading,
    isError,
  } = useQuery<CoinDetailResponse>({
    queryKey: ["coin-detail", coinId],
    queryFn: async () => {
      const response = await apiFetch({
        input: `/api/coins/details/${coinId}`,
        init: { method: "GET" },
      })
      return await response.json()
    },
    enabled: !!coinId,
    retry: true
  })

  const onRefresh = useCallback(async () => {
    setRefreshing(true)
    await queryClient.invalidateQueries({ queryKey: ["coin-detail", coinId] })
    await new Promise((resolve) => setTimeout(resolve, 600))
    setRefreshing(false)
  }, [queryClient, coinId])

  return (
    <>
      <Stack.Screen
        options={{
          title: coin?.name ?? "",
          headerBackTitle: "Voltar",
        }}
      />

      <ScrollView
        className="flex-1 bg-background"
        contentInsetAdjustmentBehavior="automatic"
        showsVerticalScrollIndicator={false}
        refreshControl={
          <RefreshControl
            refreshing={refreshing}
            onRefresh={onRefresh}
            tintColor="#a1a1aa"
            colors={["#a1a1aa"]}
            progressBackgroundColor="#18181b"
          />
        }
      >
        {isLoading ? (
          <CoinDetailSkeleton />
        ) : isError || !coin ? (
          <Animated.View entering={FadeIn.duration(400)} className="items-center justify-center py-24 gap-3 px-4">
            <Text className="text-muted-foreground text-sm">
              Não foi possível carregar os detalhes.
            </Text>
            <Text className="text-muted-foreground/60 text-xs">
              Puxe para baixo para tentar novamente.
            </Text>
          </Animated.View>
        ) : (
          <View className="px-4 pb-8 gap-5">
            <CoinDetailHeader coin={coin} />
            <CoinDetailSparkline coin={coin} />
            <CoinDetailMarketData coin={coin} />
            <CoinDetailAthAtl coin={coin} />
            <CoinDetailSentiment coin={coin} />
            <CoinDetailInfo coin={coin} />

            {coin.updatedAt && (
              <Animated.View entering={FadeIn.delay(700).duration(400)} className="items-center pb-2">
                <Text className="text-[10px] text-muted-foreground/40">
                  Atualizado em {new Intl.DateTimeFormat("pt-BR", {
                    day: "2-digit",
                    month: "short",
                    year: "numeric",
                    hour: "2-digit",
                    minute: "2-digit",
                  }).format(new Date(coin.updatedAt))}
                </Text>
              </Animated.View>
            )}
          </View>
        )}
      </ScrollView>
    </>
  )
}