import { IconBadge, type IconBadgeProps } from "./ui/icon-badge"
import { Text } from "./ui/text"
import { View } from "react-native"
import { type LucideIcon } from "lucide-react-native"

type SectionHeaderProps = {
  icon: LucideIcon
  iconVariant?: IconBadgeProps["variant"]
  title: string
  subtitle?: string
}

export function SectionHeader({ icon, iconVariant, title, subtitle }: SectionHeaderProps) {
  return (
    <View className="flex-row items-center gap-2 mb-3">
      <IconBadge icon={icon} variant={iconVariant} />
      <View>
        <Text className="text-lg font-bold tracking-tight">
          {title}
        </Text>
        {subtitle ? (
          <Text className="text-xs text-muted-foreground">
            {subtitle}
          </Text>
        ) : null}
      </View>
    </View>
  )
}
