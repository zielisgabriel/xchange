import { TrendingCoins } from "@/components/trending-coins";
import { Text } from "@/components/ui/text";
import { CoinWithMarketData } from "@/types/coin-with-market-data";
import { View } from "react-native";
import { SafeAreaView } from "react-native-safe-area-context";

export default function Index() {
  return (
    <SafeAreaView>
      <View>
        <Text className="text-foreground text-xl font-bold">
          Destaques do dia
        </Text>

        <TrendingCoins />
      </View>
    </SafeAreaView>
  )
}