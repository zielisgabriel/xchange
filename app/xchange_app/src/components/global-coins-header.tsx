import { View } from "react-native";
import Animated, { FadeInDown } from "react-native-reanimated";
import { Skeleton } from "./ui/skeleton";
import { Text } from "./ui/text";
import { IconBadge } from "./ui/icon-badge";
import { Activity, ArrowDownRight, ArrowUpRight, BarChart3 } from "lucide-react-native";
import { useQuery } from "@tanstack/react-query";
import { GlobalCoinMetrics } from "@/types/global-coin-metrics";
import clsx from "clsx";

export function GlobalCoinsHeader() {
  async function getGlobalCoinMetrics() {
    const response = await fetch("/api/coins/global", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    })

    const data: GlobalCoinMetrics = await response.json()
    return data
  }

  const {
    data: globalCoinMetrics,
    isError,
    isLoading
  } = useQuery<GlobalCoinMetrics>({
    queryKey: ["global-coin-metrics"],
    queryFn: getGlobalCoinMetrics,
  })

  const currencyFormater = Intl.NumberFormat("en-US", {
    style: "currency",
    currency: "USD",
    minimumFractionDigits: 1,
    maximumFractionDigits: 1,
    notation: "compact"
  })

  const percentageFormat = Intl.NumberFormat("en-US", {
    style: "percent",
    maximumFractionDigits: 1,
    minimumFractionDigits: 1
  })

  return (
    <Animated.View
      entering={FadeInDown.delay(200).duration(500).springify().damping(16)}
      className="px-4 -mt-6"
    >
      <View className="flex-row gap-3">
        {isLoading ? (
          <>
            <View className="flex-1 bg-card rounded-2xl p-4 gap-2 border border-border/50 shadow-sm">
              <View className="flex-row items-center gap-1.5">
                <Skeleton className="w-6 h-6 rounded-full" />
                <Skeleton className="flex-1 h-6 rounded-full" />
              </View>
              <Skeleton className="h-6 max-w-20" />
              <Skeleton className="h-4 max-w-12" />
            </View>
            <View className="flex-1 bg-card rounded-2xl p-4 gap-2 border border-border/50 shadow-sm">
              <View className="flex-row items-center gap-1.5">
                <Skeleton className="w-6 h-6 rounded-full" />
                <Skeleton className="flex-1 h-6 rounded-full" />
              </View>
              <Skeleton className="h-6 max-w-20" />
              <Skeleton className="h-4 max-w-12" />
            </View>
          </>
        ) : isError ? (
          <View className="flex-1 bg-card rounded-2xl p-4 gap-2 border border-border shadow-sm items-center justify-center">
            <Text className="text-sm text-muted-foreground">
              Não foi possível carregar os dados globais.
            </Text>
            <Text className="text-xs text-muted-foreground/60">
              Puxe para baixo para tentar novamente.
            </Text>
          </View>
        ) : (
          <>
            <View className="flex-1 bg-card rounded-2xl p-4 gap-2 border border-border/50 shadow-sm">
              <View className="flex-row items-center gap-1.5">
                <IconBadge icon={BarChart3} variant="success" size="sm" />
                <Text className="text-xs text-muted-foreground font-medium">
                  Market Cap
                </Text>
              </View>
              <Text className="text-lg font-bold">
                {currencyFormater.format(globalCoinMetrics?.data.totalMarketCap!)}
              </Text>
              <View>
                <Text className={clsx("flex flex-row text-xs items-center gap-0.5", {
                  "text-red-500": globalCoinMetrics?.data.marketCapChangePercentage24hUsd! < 0,
                  "text-green-500" : globalCoinMetrics?.data.marketCapChangePercentage24hUsd! > 0
                })}>
                  {globalCoinMetrics?.data.marketCapChangePercentage24hUsd! > 0 ? (
                    <ArrowUpRight size={12} />
                  ) : (
                    <ArrowDownRight size={12} />
                  )}
                  {percentageFormat.format(globalCoinMetrics?.data.marketCapChangePercentage24hUsd!)}
                </Text>
              </View>
            </View>

            <View className="flex-1 bg-card rounded-2xl p-4 gap-2 border border-border/50 shadow-sm">
              <View className="flex-row items-center gap-1.5">
                <IconBadge icon={Activity} variant="info" size="sm" />
                <Text className="text-xs text-muted-foreground font-medium">
                  Volume 24h
                </Text>
              </View>
              <Text className="text-lg font-bold">
                {currencyFormater.format(globalCoinMetrics?.data.totalVolume!)}
              </Text>
              <View>
                <Text className={clsx("flex flex-row text-xs items-center gap-0.5", {
                  "text-red-500": globalCoinMetrics?.data.volumeChangePercentage24hUsd! < 0,
                  "text-green-500" : globalCoinMetrics?.data.volumeChangePercentage24hUsd! > 0
                })}>
                  {globalCoinMetrics?.data.volumeChangePercentage24hUsd! > 0 ? (
                    <ArrowUpRight size={12} />
                  ) : (
                    <ArrowDownRight size={12} />
                  )}
                  {percentageFormat.format(globalCoinMetrics?.data.volumeChangePercentage24hUsd!)}
                </Text>
              </View>
            </View>
          </>
        )}
      </View>
    </Animated.View>
  )
}