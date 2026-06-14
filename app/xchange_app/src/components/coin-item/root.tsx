import { Href, router } from "expo-router";
import { ReactNode } from "react";
import { Pressable } from "react-native";
import Animated, { FadeInDown } from "react-native-reanimated";

interface CoinItemProps {
  children: ReactNode,
  coinId: string,
  index: number,
  routeUrl: Href
}

export function CoinItemRoot({children, coinId, index, routeUrl}: CoinItemProps) {
  return (
    <Animated.View
      key={coinId}
      entering={FadeInDown.delay(index * 50)
        .duration(400)
        .springify()
        .damping(18)}
    >
      <Pressable
        onPress={() => router.push(routeUrl)}
        className="flex-row w-full items-center justify-between px-2 py-3 rounded-2xl active:bg-muted/50"
      >
        {children}
      </Pressable>
    </Animated.View>
  )
}