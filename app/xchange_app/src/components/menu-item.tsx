import { Pressable, View } from "react-native";
import { Icon } from "./ui/icon";
import { Text } from "./ui/text";
import { ChevronRight, User } from "lucide-react-native";

type MenuItemProps = {
  icon: typeof User;
  label: string;
  subtitle?: string;
  onPress?: () => void;
  destructive?: boolean;
};

export function MenuItem({ icon, label, subtitle, onPress, destructive }: MenuItemProps) {
  return (
    <Pressable
      onPress={onPress}
      className="flex-row items-center px-5 py-4 active:bg-accent/50"
    >
      <View className={`w-10 h-10 rounded-xl items-center justify-center ${destructive ? "bg-destructive/10" : "bg-muted"}`}>
        <Icon
          as={icon}
          className={`size-5 ${destructive ? "text-destructive" : "text-muted-foreground"}`}
        />
      </View>
      <View className="flex-1 ml-3">
        <Text className={`text-[15px] font-medium ${destructive ? "text-destructive" : ""}`}>
          {label}
        </Text>
        {subtitle ? (
          <Text className="text-xs text-muted-foreground mt-0.5">
            {subtitle}
          </Text>
        ) : null}
      </View>
      {!destructive ? (
        <Icon as={ChevronRight} className="size-5 text-muted-foreground/50" />
      ) : null}
    </Pressable>
  );
}