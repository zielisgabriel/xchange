"use client"

import { Text } from "@/components/ui/text";
import { Icon } from "@/components/ui/icon";
import { Avatar, AvatarFallback } from "@/components/ui/avatar"
import { Separator } from "@/components/ui/separator";
import { Pressable, ScrollView, View } from "react-native";
import { useAuthStore } from "@/hooks/use-auth-store";
import { LinearGradient } from "expo-linear-gradient";
import {
  User,
  Bell,
  Shield,
  HelpCircle,
  LogOut,
  Settings,
  CameraIcon,
} from "lucide-react-native";
import { MenuItem } from "@/components/menu-item";

export default function Profile() {
  const { logOut } = useAuthStore()

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
              <Text className="text-4xl font-bold text-white">JG</Text>
            </AvatarFallback>
          </Avatar>

          <Pressable className="absolute bottom-0 right-0 w-8 h-8 bg-primary rounded-full items-center justify-center border-2 border-background">
            <Icon as={CameraIcon} className="size-3.5 text-primary-foreground" />
          </Pressable>
        </View>

        <Text className="text-xl font-bold text-white">
          José Gabriel
        </Text>
      </LinearGradient>


      <View className="mt-6">
        <Text className="text-xs font-semibold text-muted-foreground uppercase tracking-wider px-5 mb-1">
          Conta
        </Text>
        <MenuItem
          icon={User}
          label="Informações pessoais"
          subtitle="Nome, e-mail, telefone"
        />
        <MenuItem
          icon={Shield}
          label="Segurança"
          subtitle="Senha e autenticação"
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
        />
        <MenuItem
          icon={Settings}
          label="Configurações"
          subtitle="Tema, idioma, privacidade"
        />
        <MenuItem
          icon={HelpCircle}
          label="Ajuda e suporte"
          subtitle="Fale conosco"
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
  );
}