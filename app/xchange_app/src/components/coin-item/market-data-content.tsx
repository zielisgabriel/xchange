import { ReactNode } from "react";
import { View } from "react-native";

interface CoinItemMarketDataContentProps {
  children: ReactNode
}

export function CoinItemMarketDataContent({children}: CoinItemMarketDataContentProps) {
  return (
    <View className="items-end gap-0.5">
      {children}
    </View>
  )
}