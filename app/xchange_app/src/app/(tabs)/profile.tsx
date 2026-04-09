import { Button } from "@/components/ui/button";
import { Text } from "@/components/ui/text";
import { Avatar, AvatarFallback } from "@/components/ui/avatar"
import { View } from "react-native";
import { useAuthStore } from "@/hooks/use-auth-store";

export default function Profile() {
  const { logOut } = useAuthStore()

  return (
    <View className="flex-1 items-center">
      <Avatar alt="avatar" className="w-30 h-30">
        <AvatarFallback>
          <Text className="text-5xl">JG</Text>
        </AvatarFallback>
      </Avatar>

      <Text className="text-lg font-semibold">
        José Gabriel
      </Text>

      <Button
        variant={"destructive"}
        size={"sm"}
        className="max-w-40"
        onPress={logOut}
      >
        <Text>
          Sair da conta
        </Text>
      </Button>
    </View>
  )
}