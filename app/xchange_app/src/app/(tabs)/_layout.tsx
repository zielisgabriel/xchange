import { Tabs } from "expo-router";
import { HomeIcon, UserIcon } from "lucide-react-native";

export default function TabsLayout() {
  return (
    <Tabs>
      <Tabs.Screen
        name="index"
        options={{
          headerShown: false,
          title: "Início",
          tabBarIcon: ({color, size}) => <HomeIcon color={color} size={size} />
        }} 
      />
      <Tabs.Screen
        name="profile"
        options={{
          headerShown: false,
          title: "Perfil",
          tabBarIcon: ({color, size}) => <UserIcon color={color} size={size} />
        }}
      />
    </Tabs>
  )
}