import { ReactNode } from "react";
import { View } from "react-native";

interface CoinItemMarketContentProps {
  children: ReactNode
}

export function CoinItemMarketContent({children}: CoinItemMarketContentProps) {
  return (
    <View className="flex-row items-center gap-3">
      {children}
    </View>
  )
}