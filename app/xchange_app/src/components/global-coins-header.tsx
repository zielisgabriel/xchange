import { View } from "react-native";
import Animated, { FadeInDown } from "react-native-reanimated";
import { Skeleton } from "./ui/skeleton";
import { Text } from "./ui/text";
import { IconBadge } from "./ui/icon-badge";
import { Activity, ArrowDownRight, ArrowUpRight, BarChart3 } from "lucide-react-native";
import { useQuery } from "@tanstack/react-query";
import { GlobalCoinMetrics } from "@/types/global-coin-metrics";
import clsx from "clsx";
import { apiFetch } from "@/lib/api-fetch";
import { Card, CardContent, CardFooter, CardHeader } from "./ui/card";
import { formatCompactCurrency } from "@/utils/currency-format";
import { Icon } from "./ui/icon";

const percentageFormat = Intl.NumberFormat("en-US", {
  style: "percent",
  maximumFractionDigits: 1,
  minimumFractionDigits: 1
})

// API returns percentage points (e.g. 2.34 → 2.34%). Intl "percent" multiplies
// by 100, so divide before formatting to avoid showing "234%".
function formatPercentage(percentagePoints: number): string {
  return percentageFormat.format(percentagePoints / 100)
}

export function GlobalCoinsHeader() {
  async function getGlobalCoinMetrics() {
    const response = await apiFetch({
      input: "/api/coins/global",
      init: {
        method: "GET"
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

  return (
    <Animated.View
      entering={FadeInDown.delay(200).duration(500).springify().damping(16)}
      className="px-4 -mt-6"
    >
      <View className="flex-row gap-3">
        {isLoading ? (
          <>
            <Card className="flex-1">
              <CardHeader className="flex-row items-center gap-1.5">
                <Skeleton className="w-6 h-6 rounded-full" />
                <Skeleton className="flex-1 h-6 rounded-full" />
              </CardHeader>
              <CardContent>
                <Skeleton className="h-6 max-w-20" />
              </CardContent>
              <CardFooter>
                <Skeleton className="h-4 max-w-16" />
              </CardFooter>
            </Card>
            <Card className="flex-1">
              <CardHeader className="flex-row items-center gap-1.5">
                <Skeleton className="w-6 h-6 rounded-full" />
                <Skeleton className="flex-1 h-6 rounded-full" />
              </CardHeader>
              <CardContent>
                <Skeleton className="h-6 max-w-20" />
              </CardContent>
              <CardFooter>
                <Skeleton className="h-4 max-w-16" />
              </CardFooter>
            </Card>
          </>
        ) : isError || !globalCoinMetrics?.data ? (
          <Card className="flex-1 items-center justify-center">
            <CardContent>
              <Text className="text-sm text-muted-foreground">
                Não foi possível carregar os dados globais.
              </Text>
              <Text className="text-xs text-muted-foreground/60">
                Puxe para baixo para tentar novamente.
              </Text>
            </CardContent>
          </Card>
        ) : (
          <>
            <Card className="flex-1">
              <CardHeader className="flex-row items-center gap-1.5">
                <IconBadge icon={BarChart3} variant="success" size="sm" />
                <Text className="text-xs text-muted-foreground font-medium">
                  Market Cap
                </Text>
              </CardHeader>
              <CardContent>
                <Text className="text-lg font-bold">
                  {formatCompactCurrency(globalCoinMetrics?.data?.totalMarketCap ?? 0)}
                </Text>
              </CardContent>
              <CardFooter>
                <Text className={clsx("flex flex-row text-xs items-center gap-0.5", {
                  "text-red-500": globalCoinMetrics?.data.marketCapChangePercentage24hUsd! < 0,
                  "text-green-500" : globalCoinMetrics?.data.marketCapChangePercentage24hUsd! > 0
                })}>
                  {globalCoinMetrics?.data.marketCapChangePercentage24hUsd! > 0 ? (
                    <Icon as={ArrowUpRight} size={16} className="text-green-500" />
                  ) : (
                    <Icon as={ArrowDownRight} size={16} className="text-red-500" />
                  )}
                  {formatPercentage(globalCoinMetrics?.data.marketCapChangePercentage24hUsd!)}
                </Text>
              </CardFooter>
            </Card>

            <Card className="flex-1">
              <CardHeader className="flex-row items-center gap-1.5">
                <IconBadge icon={Activity} variant="info" size="sm" />
                <Text className="text-xs text-muted-foreground font-medium">
                  Volume 24h
                </Text>
              </CardHeader>
              <CardContent>
                <Text className="text-lg font-bold">
                  {formatCompactCurrency(globalCoinMetrics?.data.totalVolume!)}
                </Text>
              </CardContent>
              <CardFooter>
                <Text className={clsx("flex flex-row text-xs items-center gap-0.5", {
                  "text-red-500": globalCoinMetrics?.data.volumeChangePercentage24hUsd! < 0,
                  "text-green-500" : globalCoinMetrics?.data.volumeChangePercentage24hUsd! > 0
                })}>
                  {globalCoinMetrics?.data.volumeChangePercentage24hUsd! > 0 ? (
                    <Icon as={ArrowUpRight} size={16} className="text-green-500" />
                  ) : (
                    <Icon as={ArrowDownRight} size={16} className="text-red-500" />
                  )}
                  {formatPercentage(globalCoinMetrics?.data.volumeChangePercentage24hUsd!)}
                </Text>
              </CardFooter>
            </Card>
          </>
        )}
      </View>
    </Animated.View>
  )
}
