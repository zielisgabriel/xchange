import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "./ui/card";
import { View, Text, ActivityIndicator } from "react-native";
import { LineChartBicolor } from "react-native-gifted-charts";
import { useQuery } from "@tanstack/react-query";
import { apiFetch } from "@/lib/api-fetch";

interface FavoriteCoinChartProps {
  coinId?: string;
}

export function FavoriteCoinChart({ coinId }: FavoriteCoinChartProps) {
  const { data, isLoading, isError } = useQuery({
    queryKey: ["coin-chart", coinId],
    queryFn: async () => {
      if (!coinId) return null;
      const res = await apiFetch({
        input: `/api/coins/chart/${coinId}`,
        init: { method: "GET" },
      });
      if (!res.ok) throw new Error("Failed to fetch chart data");
      return res.json();
    },
    enabled: !!coinId,
  });

  if (!coinId) return null;

  if (isLoading) {
    return (
      <Card className="mt-4 h-[250px] items-center justify-center">
        <ActivityIndicator size="small" />
        <Text className="mt-2 text-muted-foreground text-sm">Carregando gráfico de 24h...</Text>
      </Card>
    );
  }

  if (isError || !data || !data.prices || data.prices.length === 0) {
    return (
      <Card className="mt-4 h-[250px] items-center justify-center">
        <Text className="text-muted-foreground text-sm">Não foi possível carregar o gráfico.</Text>
      </Card>
    );
  }

  const prices = data.prices.map((i: [number, number]) => i[1]);
  const firstPrice = prices[0];
  const currentPrice = prices[prices.length - 1];
  
  const isUp = currentPrice >= firstPrice;
  const variation = ((currentPrice - firstPrice) / firstPrice) * 100;

  const chartData = data.prices.map((item: [number, number], index: number) => {
    const date = new Date(item[0]);
    const showLabel = index % 4 === 0 || index === data.prices.length - 1;
    
    return {
      value: item[1] - firstPrice,
      labelComponent: showLabel 
        ? () => <Text className="text-muted-foreground text-[9px] w-8 -ml-3">{`${date.getHours().toString().padStart(2, '0')}:00`}</Text>
        : undefined,
    };
  });

  const maxDiff = Math.max(...chartData.map((d: any) => d.value));
  const minDiff = Math.min(...chartData.map((d: any) => d.value));
  
  const maxValue = maxDiff > 0 ? maxDiff * 1.2 : 1;
  const mostNegativeValue = minDiff < 0 ? minDiff * 1.2 : 0;

  return (
    <Card className="mt-4">
      <CardHeader className="pb-2">
        <CardTitle className="text-lg">Variação de 24 Horas</CardTitle>
        <CardDescription>
          O gráfico mostra as oscilações acima e abaixo do preço de abertura.
        </CardDescription>
      </CardHeader>
      <CardContent>
        <View className="mb-6 flex-row items-end justify-between">
          <View>
            <Text className="text-3xl font-bold text-foreground">
              ${currentPrice.toLocaleString("en-US", { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
            </Text>
            <Text className={`text-sm font-medium mt-1 ${isUp ? "text-green-500" : "text-red-500"}`}>
              {isUp ? "+" : ""}{variation.toFixed(2)}% nas últimas 24h
            </Text>
          </View>
        </View>

        <View className="overflow-hidden rounded-xl bg-card/50 items-center w-full py-4 border border-border mt-2">
          <LineChartBicolor
            data={chartData}
            areaChart
            curved
            color="rgb(34, 197, 94)" 
            colorNegative="rgb(239, 68, 68)"
            startFillColor="rgba(34, 197, 94, 0.4)"
            endFillColor="rgba(34, 197, 94, 0.05)"
            startFillColorNegative="rgba(239, 68, 68, 0.4)"
            endFillColorNegative="rgba(239, 68, 68, 0.05)"
            thickness={2.5}
            width={260}
            height={160}
            maxValue={maxValue}
            mostNegativeValue={mostNegativeValue}
            noOfSections={4}
            disableScroll
            yAxisThickness={0}
            xAxisThickness={1}
            xAxisColor="rgba(255,255,255,0.2)"
            rulesColor="rgba(255,255,255,0.05)"
            yAxisTextStyle={{ color: "gray", fontSize: 10 }}
            yAxisLabelPrefix="$"
            formatYLabel={(val) => {
              const realPrice = Number(val) + firstPrice;
              return realPrice.toLocaleString("en-US", { maximumFractionDigits: 1 });
            }}
            initialSpacing={10}
            endSpacing={10}
            spacing={11}
            hideDataPoints
          />
        </View>
      </CardContent>
    </Card>
  );
}