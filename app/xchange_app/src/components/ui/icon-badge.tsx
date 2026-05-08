import { cn } from "@/lib/utils"
import { Icon } from "./icon"
import { View } from "react-native"
import { type LucideIcon } from "lucide-react-native"
import { cva, type VariantProps } from "class-variance-authority"

const iconBadgeVariants = cva(
  "items-center justify-center rounded-xl",
  {
    variants: {
      size: {
        sm: "w-7 h-7",
        default: "w-8 h-8",
        lg: "w-10 h-10",
      },
      variant: {
        default: "bg-primary",
        muted: "bg-muted",
        destructive: "bg-destructive/10",
        success: "bg-green-500/10",
        info: "bg-blue-500/10",
        warning: "bg-yellow-500/10",
      },
    },
    defaultVariants: {
      size: "default",
      variant: "default",
    },
  }
)

const iconColorMap: Record<string, string> = {
  default: "text-primary-foreground",
  muted: "text-muted-foreground",
  destructive: "text-destructive",
  success: "text-green-500",
  info: "text-blue-500",
  warning: "text-yellow-500",
}

const iconSizeMap: Record<string, string> = {
  sm: "size-3.5",
  default: "size-4",
  lg: "size-5",
}

type IconBadgeProps = {
  icon: LucideIcon
  className?: string
} & VariantProps<typeof iconBadgeVariants>

function IconBadge({ icon, size, variant, className }: IconBadgeProps) {
  const iconColor = iconColorMap[variant ?? "default"]
  const iconSize = iconSizeMap[size ?? "default"]

  return (
    <View className={cn(iconBadgeVariants({ size, variant }), className)}>
      <Icon as={icon} className={cn(iconSize, iconColor)} />
    </View>
  )
}

export { IconBadge, iconBadgeVariants }
export type { IconBadgeProps }
