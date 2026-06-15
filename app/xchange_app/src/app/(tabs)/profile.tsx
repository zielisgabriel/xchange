"use client"

import { Text } from "@/components/ui/text"
import { Icon } from "@/components/ui/icon"
import { Avatar, AvatarFallback } from "@/components/ui/avatar"
import { Separator } from "@/components/ui/separator"
import { Pressable, ScrollView, View } from "react-native"
import { useAuthStore } from "@/hooks/use-auth-store"
import { Gradient as LinearGradient } from "@/components/ui/gradient"
import { useRouter } from "expo-router"
import {
  User,
  Bell,
  Shield,
  LogOut,
  Settings
} from "lucide-react-native"
import { MenuItem } from "@/components/menu-item"
import { useProfileSimple } from "@/hooks/use-profile-simple"
import { Skeleton } from "@/components/ui/skeleton"
import { toast } from "sonner-native"

export default function Profile() {
  const router = useRouter()
  const { logOut } = useAuthStore()
  const { data: profileSimple, isLoading, isError } = useProfileSimple()

  function notifyComingSoon() {
    toast("Em breve")
  }

  return (
    <ScrollView
      className="flex-1 bg-background"
      contentContainerClassName="pb-10"
      showsVerticalScrollIndicator={false}
    >
      <LinearGradient
        colors={["hsl(0, 0%, 14%)", "hsl(0, 0%, 6%)"]}
        start={{ x: 0, y: 0 }}
        end={{ x: 1, y: 1 }}
        className="pt-16 pb-10 items-center rounded-b-[32px]"
      >
        <View className="relative mb-4">
          <Avatar alt="avatar" className="w-24 h-24 border-[3px] border-white/20">
            <AvatarFallback className="bg-white/10">
              <Icon as={User} className="w-[50%] h-[50%]" />
            </AvatarFallback>
          </Avatar>
        </View>

        {isLoading ? (
          <Skeleton className="h-8 w-32" />
        ) : (
          <Text className="text-xl font-bold text-foreground">
            {isError ? "Usuário" : profileSimple?.firstName}
          </Text>
        )}
      </LinearGradient>

      <View className="mt-6">
        <Text className="text-xs font-semibold text-muted-foreground uppercase tracking-wider px-5 mb-1">
          Conta
        </Text>
        <MenuItem
          icon={User}
          label="Informações pessoais"
          subtitle="Nome, e-mail, telefone"
          onPress={() => router.push("/profile/info")}
        />
        <MenuItem
          icon={Shield}
          label="Segurança"
          subtitle="Senha e autenticação"
          onPress={notifyComingSoon}
        />
      </View>

      <View className="px-5 my-2">
        <Separator />
      </View>

      <View>
        <Text className="text-xs font-semibold text-muted-foreground uppercase tracking-wider px-5 mb-1">
          Preferências
        </Text>
        <MenuItem
          icon={Bell}
          label="Notificações"
          subtitle="Push, e-mail, SMS"
          onPress={notifyComingSoon}
        />
        <MenuItem
          icon={Settings}
          label="Configurações"
          subtitle="Tema, idioma, privacidade"
          onPress={notifyComingSoon}
        />
      </View>

      <View className="px-5 my-2">
        <Separator />
      </View>

      <MenuItem
        icon={LogOut}
        label="Sair da conta"
        destructive
        onPress={logOut}
      />
    </ScrollView>
  )
}