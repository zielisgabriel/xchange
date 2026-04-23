import { Text } from "@/components/ui/text";
import { View } from "react-native";
import { SafeAreaView } from "react-native-safe-area-context";


export default function Index() {
  return (
    <SafeAreaView>
      <View>
        <Text className="text-foreground">
          Home screen
        </Text>
      </View>
    </SafeAreaView>
  )
}