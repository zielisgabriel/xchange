"use client"

import { Stack } from "expo-router"

export default function ProfileLayout() {
  return (
    <Stack
      screenOptions={{
        headerLargeTitle: true,
        headerLargeTitleShadowVisible: false,
        headerShadowVisible: false,
        headerBackButtonDisplayMode: "minimal",
        headerStyle: { backgroundColor: "hsl(0, 0%, 5%)" },
        headerLargeStyle: { backgroundColor: "hsl(0, 0%, 5%)" },
        headerTintColor: "#fff",
        headerTitleStyle: { fontFamily: "Sora-SemiBold", color: "#fff" },
        headerLargeTitleStyle: { fontFamily: "Sora-Bold", color: "#fff" },
        contentStyle: { backgroundColor: "hsl(0, 0%, 5%)" },
      }}
    >
      <Stack.Screen name="info" options={{ title: "Informações pessoais" }} />
    </Stack>
  )
}
