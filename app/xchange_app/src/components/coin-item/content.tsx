import { ReactNode } from "react"
import { View } from "react-native"

interface CoinItemContentProps {
  children: ReactNode
}

export function CoinItemContent({children}: CoinItemContentProps) {
  return (
    <View className="flex-row items-center gap-2.5">
      {children}
    </View>
  )
}