import { TrendingUp } from "lucide-react-native";
import { Icon } from "./ui/icon";
import { Text } from "./ui/text";
import { View } from "react-native";

export function FeatureItem({ icon, title, description }: {
  icon: typeof TrendingUp;
  title: string;
  description: string;
}) {
  return (
    <View className="flex-row items-center gap-3">
      <View className="w-10 h-10 rounded-xl bg-white/10 items-center justify-center">
        <Icon as={icon} className="size-5 text-foreground/80" />
      </View>
      <View className="flex-1">
        <Text className="text-sm font-semibold text-foreground">{title}</Text>
        <Text className="text-xs text-foreground/45">{description}</Text>
      </View>
    </View>
  )
}