import { apiFetch } from "@/lib/api-fetch"
import { CoinChartDataResponse } from "@/types/coin-chart-data-response"
import { Minute } from "@/valueobject/Minute"
import { useQuery } from "@tanstack/react-query"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "./ui/card"
import { Text } from "./ui/text"
import { Loading } from "./ui/loading"
import { LineChart } from "react-native-gifted-charts"
import { currencyFormat } from "@/utils/currency-format"
import { Dimensions, View, Text as RNText } from "react-native"
interface FavoriteCoinChartProps {
  coinId: string
}

export function FavoriteCoinChart({coinId}: FavoriteCoinChartProps) {
  const { data: chartData, isLoading } = useQuery<CoinChartDataResponse>({
    queryFn: async () => {
      const response = await apiFetch({
        input: `/api/coins/chart/${coinId}`,
        init: {
          method: "GET"
        }
      })

      if (!response.ok) return []

      const data = await response.json()

      return data
    },
    queryKey: ["coin-chart", coinId],
    refetchInterval: new Minute(5).toMilliseconds()
  })

  return (
    <Card>
      <CardHeader>
        <CardTitle>
          <Text>Gráfico de preço</Text>
        </CardTitle>
        <CardDescription>
          <Text>Nas últimas 24h</Text>
        </CardDescription>
      </CardHeader>

      <CardContent>
        {isLoading ? (
          <Loading />
        ) : (
          <View>
            <LineChart
              data={chartData?.prices?.map((price, index, arr) => {
                const date = new Date(price[0]);
                const timeString = `${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`;
                
                const interval = Math.max(1, Math.floor(arr.length / 5));
                const showLabel = index % interval === 0;

                return {
                  value: price[1],
                  label: showLabel ? timeString : '',
                  dateText: timeString,
                };
              }) ?? []}
              thickness={3}
              yAxisColor={"transparent"}
              xAxisColor={"transparent"}
              yAxisThickness={0}
              xAxisThickness={0}
              rulesColor={"rgba(255, 255, 255, 0.05)"}
              rulesType="solid"
              initialSpacing={10}
              endSpacing={10}
              color={"#f5a623"}
              areaChart={true}
              startFillColor={"rgba(245, 166, 35, 0.6)"}
              endFillColor={"rgba(245, 166, 35, 0.0)"}
              startOpacity={0.8}
              endOpacity={0}
              hideDataPoints={true}
              showVerticalLines={false}
              curved={false}
              isAnimated={true}
              animationDuration={1500}
              yAxisTextStyle={{ color: "rgba(255, 255, 255, 0.3)", fontSize: 10 }}
              xAxisLabelTextStyle={{ color: "rgba(255, 255, 255, 0.3)", fontSize: 10, textAlign: 'center' }}
              yAxisOffset={chartData?.prices ? Math.min(...chartData.prices.map(p => p[1])) * 0.995 : 0}
              width={Dimensions.get("window").width - 100}
              adjustToWidth={true}
              pointerConfig={{
                pointerStripHeight: 160,
                pointerStripColor: 'rgba(255, 255, 255, 0.3)',
                pointerStripWidth: 2,
                pointerColor: '#f5a623',
                radius: 6,
                pointerLabelWidth: 100,
                pointerLabelHeight: 50,
                activatePointersOnLongPress: false,
                autoAdjustPointerLabelPosition: true,
                pointerLabelComponent: (items: any) => {
                  const val = items[0].value;
                  const displayValue = val > 1 
                    ? new Intl.NumberFormat("en-US", { style: "currency", currency: "USD", maximumFractionDigits: 2 }).format(val)
                    : currencyFormat.format(val);

                  return (
                    <View
                      style={{
                        height: 50,
                        width: 100,
                        justifyContent: 'center',
                        backgroundColor: '#1E1E1E',
                        borderRadius: 8,
                        borderWidth: 1,
                        borderColor: 'rgba(245, 166, 35, 0.4)',
                        shadowColor: "#000",
                        shadowOffset: { width: 0, height: 4 },
                        shadowOpacity: 0.5,
                        shadowRadius: 4,
                        elevation: 5,
                      }}>
                      <RNText style={{ color: '#f5a623', fontSize: 13, fontWeight: 'bold', textAlign: 'center' }}>
                        {displayValue}
                      </RNText>
                      <RNText style={{ color: 'rgba(255, 255, 255, 0.6)', fontSize: 10, textAlign: 'center', marginTop: 2 }}>
                        {items[0].dateText}
                      </RNText>
                    </View>
                  );
                },
              }}
            />
          </View>
        )}
      </CardContent>
    </Card>
  )
}