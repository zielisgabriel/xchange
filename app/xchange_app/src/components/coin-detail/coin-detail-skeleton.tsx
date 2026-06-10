import { View } from "react-native"
import { Skeleton } from "@/components/ui/skeleton"
import Animated, { FadeIn } from "react-native-reanimated"

export function CoinDetailSkeleton() {
  return (
    <Animated.View entering={FadeIn.duration(400)} className="px-4 pt-4 gap-6">
      <View className="items-center gap-3">
        <Skeleton className="w-16 h-16 rounded-full" />
        <Skeleton className="w-24 h-5 rounded" />
        <Skeleton className="w-14 h-4 rounded" />
        <Skeleton className="w-36 h-9 rounded-lg" />
        <Skeleton className="w-20 h-6 rounded-full" />
      </View>

      <View className="bg-card border border-border rounded-2xl p-4 gap-3">
        <Skeleton className="w-24 h-3 rounded" />
        <Skeleton className="w-full h-[140px] rounded-lg" />
      </View>

      <View className="flex-row gap-2">
        {Array.from({ length: 4 }).map((_, i) => (
          <View key={i} className="flex-1 bg-card border border-border rounded-xl py-3 items-center gap-2">
            <Skeleton className="w-8 h-2 rounded" />
            <Skeleton className="w-12 h-4 rounded" />
          </View>
        ))}
      </View>

      <View className="bg-card border border-border rounded-2xl px-4">
        {Array.from({ length: 3 }).map((_, i) => (
          <View key={i} className="flex-row items-center justify-between py-3">
            <View className="flex-row items-center gap-2.5">
              <Skeleton className="w-8 h-8 rounded-xl" />
              <Skeleton className="w-20 h-3 rounded" />
            </View>
            <Skeleton className="w-24 h-3 rounded" />
          </View>
        ))}
      </View>

      <View className="flex-row gap-3">
        <View className="flex-1 bg-card border border-border rounded-2xl p-4 gap-2">
          <Skeleton className="w-8 h-8 rounded-xl" />
          <Skeleton className="w-20 h-5 rounded" />
          <Skeleton className="w-12 h-3 rounded" />
        </View>
        <View className="flex-1 bg-card border border-border rounded-2xl p-4 gap-2">
          <Skeleton className="w-8 h-8 rounded-xl" />
          <Skeleton className="w-20 h-5 rounded" />
          <Skeleton className="w-12 h-3 rounded" />
        </View>
      </View>
    </Animated.View>
  )
}
