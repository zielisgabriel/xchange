import { ReactNode } from "react";
import { View } from "react-native";

interface CoinItemTitleContentProps {
  children: ReactNode
}

export function CoinItemTitleContent({children}: CoinItemTitleContentProps) {
  return (
    <View>
      {children}
    </View>
  )
}