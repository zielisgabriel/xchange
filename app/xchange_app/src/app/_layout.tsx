import "@/global.css";

import { ThemeProvider } from "@react-navigation/native";
import { Stack } from "expo-router";
import { PortalHost } from "@rn-primitives/portal";
import { NAV_THEME } from "@/lib/theme";
import { useEffect } from "react";
import * as SplashScreen from "expo-splash-screen";
import {
  useFonts,
  Sora_100Thin,
  Sora_200ExtraLight,
  Sora_300Light,
  Sora_400Regular,
  Sora_500Medium,
  Sora_600SemiBold,
  Sora_700Bold,
  Sora_800ExtraBold,
} from "@expo-google-fonts/sora";
import { useAuthStore } from "@/hooks/use-auth-store";
import { GestureHandlerRootView } from "react-native-gesture-handler";
import { Toaster } from "sonner-native"

SplashScreen.preventAutoHideAsync();

export default function Layout() {
  const { isAuthenticated } = useAuthStore()

  const [loaded, error] = useFonts({
    "Sora-Thin": Sora_100Thin,
    "Sora-ExtraLight": Sora_200ExtraLight,
    "Sora-Light": Sora_300Light,
    "Sora-Regular": Sora_400Regular,
    "Sora-Medium": Sora_500Medium,
    "Sora-SemiBold": Sora_600SemiBold,
    "Sora-Bold": Sora_700Bold,
    "Sora-ExtraBold": Sora_800ExtraBold,
  });

  useEffect(() => {
    if (loaded || error) {
      SplashScreen.hideAsync();
    }
  }, [loaded, error]);

  if (!loaded && !error) {
    return null;
  }

  return (
    <GestureHandlerRootView>
      <ThemeProvider value={NAV_THEME["dark"]}>
        <Stack>
          <Stack.Protected guard={isAuthenticated}>
            <Stack.Screen name="(tabs)" options={{ headerShown: false }} />
          </Stack.Protected>
          <Stack.Protected guard={!isAuthenticated}>
            <Stack.Screen name="auth" options={{ headerShown: false }} />
            <Stack.Screen name="login" options={{ headerShown: false }} />
            <Stack.Screen name="register" options={{ headerShown: false }} />
          </Stack.Protected>
        </Stack>
        <Toaster />
        <PortalHost />
      </ThemeProvider>
    </GestureHandlerRootView>
  );
}