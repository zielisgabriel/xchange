"use client"

import { View } from "react-native"
import { Button } from "@/components/ui/button"
import { Separator } from "@/components/ui/separator"
import { Text } from "@/components/ui/text"
import { Icon } from "@/components/ui/icon"
import { Link } from "expo-router"
import { LinearGradient } from "expo-linear-gradient"
import {
  ArrowRight,
  UserPlus,
  TrendingUp,
  Shield
} from "lucide-react-native"
import { FeatureItem } from "@/components/feature-item"

export default function Auth() {
  return (
    <LinearGradient
      colors={["hsl(0, 0%, 8%)", "hsl(0, 0%, 3%)"]}
      className="flex-1"
    >
      <View className="flex-1 justify-between px-6 pt-24 pb-10">
        <View className="items-center mt-10">
          <Text className="text-foreground font-sora-extrabold tracking-widest text-4xl">
            XCHANGE
          </Text>
          <Text className="text-white/50 font-light text-sm text-center mt-2 leading-5">
            Plataforma para investidores de cripto
          </Text>
        </View>

        <View className="gap-4 px-2">
          <FeatureItem
            icon={TrendingUp}
            title="Acompanhe o mercado"
            description="Cotações em tempo real"
          />
          <FeatureItem
            icon={Shield}
            title="Segurança total"
            description="Seus dados protegidos"
          />
        </View>

        <View>
          <Link href="/login" asChild>
            <Button variant="default" size="lg" className="mb-3">
              <Text>Fazer Login</Text>
              <Icon as={ArrowRight} className="size-4 text-primary-foreground" />
            </Button>
          </Link>

          <Link href="/register" asChild>
            <Button variant="outline" size="lg" className="border-white/15 bg-white/5">
              <Icon as={UserPlus} className="size-4 text-white" />
              <Text className="text-white">Criar Conta</Text>
            </Button>
          </Link>

          <Separator className="my-5 bg-white/10" />

          <Text className="text-white/30 text-center text-[10px] leading-4">
            Ao continuar, você concorda com nossos{"\n"}
            <Text className="text-blue-400 text-[10px]">
              <Link href="/">Termos de Uso</Link>
            </Text>
            {" e "}
            <Text className="text-blue-400 text-[10px]">
              <Link href="/">Política de Privacidade</Link>
            </Text>
          </Text>
        </View>

      </View>
    </LinearGradient>
  )
}