import "@/global.css"
import { useEffect } from "react"
import { View, Text, Dimensions, Image } from "react-native"
import { GestureHandlerRootView } from "react-native-gesture-handler"
import { LinearGradient } from "expo-linear-gradient"
import Animated, {
  useSharedValue,
  useAnimatedStyle,
  withTiming,
  withDelay,
  withRepeat,
  withSequence,
  Easing,
  FadeIn,
  FadeInDown,
  FadeInUp,
} from "react-native-reanimated"
import { Button } from "@/components/ui/button"

const { width: SCREEN_WIDTH } = Dimensions.get("window")


function FloatingOrb({ delay, size, x, y, color1, color2 }: {
  delay: number
  size: number
  x: number
  y: number
  color1: string
  color2: string
}) {
  const translateY = useSharedValue(0)
  const opacity = useSharedValue(0)

  useEffect(() => {
    opacity.value = withDelay(delay, withTiming(0.4, { duration: 1200 }))
    translateY.value = withDelay(
      delay,
      withRepeat(
        withSequence(
          withTiming(-18, { duration: 3000, easing: Easing.inOut(Easing.ease) }),
          withTiming(18, { duration: 3000, easing: Easing.inOut(Easing.ease) })
        ),
        -1,
        true
      )
    )
  }, [])

  const animatedStyle = useAnimatedStyle(() => ({
    transform: [{ translateY: translateY.value }],
    opacity: opacity.value,
  }))

  return (
    <Animated.View
      style={[
        {
          position: "absolute",
          left: x,
          top: y,
          width: size,
          height: size,
          borderRadius: size / 2,
        },
        animatedStyle,
      ]}
    >
      <LinearGradient
        colors={[color1, color2]}
        style={{
          width: size,
          height: size,
          borderRadius: size / 2,
        }}
        start={{ x: 0, y: 0 }}
        end={{ x: 1, y: 1 }}
      />
    </Animated.View>
  )
}

function GlowLine({ delay }: { delay: number }) {
  const width = useSharedValue(0)
  const opacity = useSharedValue(0)

  useEffect(() => {
    opacity.value = withDelay(delay, withTiming(1, { duration: 600 }))
    width.value = withDelay(
      delay,
      withTiming(120, { duration: 1000, easing: Easing.out(Easing.cubic) })
    )
  }, [])

  const animatedStyle = useAnimatedStyle(() => ({
    width: width.value,
    opacity: opacity.value,
  }))

  return (
    <Animated.View
      style={[
        {
          height: 1,
          borderRadius: 1,
          backgroundColor: "rgba(0, 212, 255, 0.3)",
        },
        animatedStyle,
      ]}
    />
  )
}

export default function Index() {
  const logoScale = useSharedValue(0.6)
  const logoOpacity = useSharedValue(0)
  const glowOpacity = useSharedValue(0)

  useEffect(() => {
    logoOpacity.value = withDelay(200, withTiming(1, { duration: 800 }))
    logoScale.value = withDelay(
      200,
      withTiming(1, { duration: 1000, easing: Easing.out(Easing.back(1.4)) })
    )
    glowOpacity.value = withDelay(
      800,
      withRepeat(
        withSequence(
          withTiming(0.6, { duration: 2000, easing: Easing.inOut(Easing.ease) }),
          withTiming(0.2, { duration: 2000, easing: Easing.inOut(Easing.ease) })
        ),
        -1,
        true
      )
    )
  }, [])

  const logoAnimatedStyle = useAnimatedStyle(() => ({
    transform: [{ scale: logoScale.value }],
    opacity: logoOpacity.value,
  }))

  const glowAnimatedStyle = useAnimatedStyle(() => ({
    opacity: glowOpacity.value,
  }))

  return (
    <GestureHandlerRootView className="flex-1">
      <View className="flex-1 bg-background">
        {/* Background gradient overlay */}
        <LinearGradient
          colors={["#000000", "#050510", "#0a0a1a", "#000000"]}
          style={{
            position: "absolute",
            width: "100%",
            height: "100%",
          }}
          start={{ x: 0, y: 0 }}
          end={{ x: 1, y: 1 }}
        />

        {/* Floating ambient orbs */}
        <FloatingOrb delay={0} size={180} x={-40} y={80} color1="rgba(0, 212, 255, 0.08)" color2="rgba(123, 47, 255, 0.04)" />
        <FloatingOrb delay={400} size={140} x={SCREEN_WIDTH - 80} y={200} color1="rgba(123, 47, 255, 0.06)" color2="rgba(0, 212, 255, 0.03)" />
        <FloatingOrb delay={800} size={100} x={60} y={600} color1="rgba(0, 212, 255, 0.05)" color2="rgba(123, 47, 255, 0.08)" />
        <FloatingOrb delay={200} size={220} x={SCREEN_WIDTH - 160} y={500} color1="rgba(123, 47, 255, 0.04)" color2="rgba(0, 212, 255, 0.02)" />

        {/* Main content */}
        <View className="flex-1 justify-between items-center px-8 pt-24 pb-16">
          {/* Top section - Brand */}
          <View className="items-center mt-16">
            {/* Logo with animated glow */}
            <View className="items-center justify-center mb-6">
              <Animated.View style={[glowAnimatedStyle, {
                position: "absolute",
                width: 160,
                height: 160,
                borderRadius: 80,
                backgroundColor: "transparent",
                shadowColor: "#00D4FF",
                shadowOffset: { width: 0, height: 0 },
                shadowOpacity: 0.5,
                shadowRadius: 40,
                elevation: 20,
              }]} />

              <Animated.View style={logoAnimatedStyle}>
                <View className="items-center justify-center" style={{
                  width: 120,
                  height: 120,
                  borderRadius: 30,
                  overflow: "hidden",
                }}>
                  <Image
                    source={require("@/../assets/images/xchange-logo.png")}
                    style={{ width: 120, height: 120 }}
                    resizeMode="contain"
                  />
                </View>
              </Animated.View>
            </View>

            {/* Brand name with stagger */}
            <Animated.View entering={FadeInDown.delay(500).duration(800).springify()}>
              <Text
                className="text-foreground font-sora-extrabold"
                style={{ fontSize: 42, letterSpacing: 3 }}
              >
                XCHANGE
              </Text>
            </Animated.View>

            {/* Decorative line */}
            <Animated.View
              entering={FadeIn.delay(800).duration(600)}
              className="items-center mt-4 mb-5"
            >
              <GlowLine delay={900} />
            </Animated.View>

            {/* Tagline */}
            <Animated.View entering={FadeInDown.delay(900).duration(800).springify()}>
              <Text
                className="text-foreground-muted font-sora-light text-center"
                style={{ fontSize: 15, letterSpacing: 1.5, lineHeight: 22 }}
              >
                Plataforma para investidores de cripto
              </Text>
            </Animated.View>
          </View>

          {/* Middle section - Feature highlights */}
          <Animated.View
            entering={FadeInUp.delay(1100).duration(800).springify()}
            className="items-center w-full"
          >
            <View className="flex-row justify-center items-center gap-8 mb-8">
              <View className="items-center">
                <View style={{
                  width: 48,
                  height: 48,
                  borderRadius: 14,
                  backgroundColor: "rgba(0, 212, 255, 0.08)",
                  borderWidth: 1,
                  borderColor: "rgba(0, 212, 255, 0.15)",
                  justifyContent: "center",
                  alignItems: "center",
                  marginBottom: 8,
                }}>
                  <Text style={{ fontSize: 20 }}>⚡</Text>
                </View>
                <Text className="text-foreground-muted font-sora text-xs">
                  Rápido
                </Text>
              </View>

              <View className="items-center">
                <View style={{
                  width: 48,
                  height: 48,
                  borderRadius: 14,
                  backgroundColor: "rgba(123, 47, 255, 0.08)",
                  borderWidth: 1,
                  borderColor: "rgba(123, 47, 255, 0.15)",
                  justifyContent: "center",
                  alignItems: "center",
                  marginBottom: 8,
                }}>
                  <Text style={{ fontSize: 20 }}>🔒</Text>
                </View>
                <Text className="text-foreground-muted font-sora text-xs">
                  Seguro
                </Text>
              </View>

              <View className="items-center">
                <View style={{
                  width: 48,
                  height: 48,
                  borderRadius: 14,
                  backgroundColor: "rgba(0, 212, 255, 0.06)",
                  borderWidth: 1,
                  borderColor: "rgba(0, 212, 255, 0.12)",
                  justifyContent: "center",
                  alignItems: "center",
                  marginBottom: 8,
                }}>
                  <Text style={{ fontSize: 20 }}>🌍</Text>
                </View>
                <Text className="text-foreground-muted font-sora text-xs">
                  Global
                </Text>
              </View>
            </View>
          </Animated.View>

          {/* Bottom section - CTA Buttons */}
          <Animated.View
            entering={FadeInDown.delay(1300).duration(800).springify()}
            className="w-full items-center"
          >
            {/* Primary CTA - Login */}
            <View className="w-full mb-4" style={{ maxWidth: 320 }}>
              <LinearGradient
                colors={["#00D4FF", "#7B2FFF"]}
                start={{ x: 0, y: 0 }}
                end={{ x: 1, y: 0 }}
                style={{
                  borderRadius: 9999,
                  padding: 1,
                }}
              >
                <Button
                  variant="ghost"
                  size="lg"
                  className="w-full rounded-full"
                  textClassName="text-foreground font-sora-semibold"
                  style={{
                    backgroundColor: "rgba(0, 0, 0, 0.85)",
                  }}
                >
                  Fazer Login
                </Button>
              </LinearGradient>
            </View>

            {/* Separator */}
            <View className="flex-row items-center mb-4 w-full" style={{ maxWidth: 320 }}>
              <View style={{ flex: 1, height: 1, backgroundColor: "rgba(255, 255, 255, 0.06)" }} />
              <Text className="text-foreground-muted font-sora-light mx-4" style={{ fontSize: 12 }}>
                ou
              </Text>
              <View style={{ flex: 1, height: 1, backgroundColor: "rgba(255, 255, 255, 0.06)" }} />
            </View>

            {/* Secondary CTA - Register */}
            <View className="w-full" style={{ maxWidth: 320 }}>
              <Button
                variant="outline"
                size="lg"
                className="w-full rounded-full"
                textClassName="font-sora-semibold"
                style={{
                  borderColor: "rgba(123, 47, 255, 0.4)",
                  borderWidth: 1,
                }}
              >
                Criar Conta
              </Button>
            </View>

            {/* Terms text */}
            <Animated.View entering={FadeIn.delay(1800).duration(800)}>
              <Text
                className="text-foreground-muted font-sora-light text-center mt-6"
                style={{ fontSize: 11, opacity: 0.5, lineHeight: 16 }}
              >
                Ao continuar, você concorda com nossos{"\n"}
                <Text style={{ color: "rgba(0, 212, 255, 0.7)" }}>Termos de Uso</Text>
                {" e "}
                <Text style={{ color: "rgba(0, 212, 255, 0.7)" }}>Política de Privacidade</Text>
              </Text>
            </Animated.View>
          </Animated.View>
        </View>
      </View>
    </GestureHandlerRootView>
  )
}