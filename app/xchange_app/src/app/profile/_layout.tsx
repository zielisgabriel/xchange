"use client"

import { Stack } from "expo-router"

export default function ProfileLayout() {
  return (
    <Stack
      screenOptions={{
        headerStyle: { backgroundColor: "hsl(0, 0%, 5%)" },
        headerTintColor: "#fff",
        headerTitleStyle: { fontFamily: "Sora-SemiBold" },
        headerShadowVisible: false,
        contentStyle: { backgroundColor: "hsl(0, 0%, 5%)" },
      }}
    >
      <Stack.Screen name="info" options={{ title: "Informações pessoais" }} />
      <Stack.Screen name="security" options={{ title: "Segurança" }} />
      <Stack.Screen name="notifications" options={{ title: "Notificações" }} />
      <Stack.Screen name="settings" options={{ title: "Configurações" }} />
    </Stack>
  )
}
