"use client"

import { Text } from "@/components/ui/text"
import { Input } from "@/components/ui/input"
import { Separator } from "@/components/ui/separator"
import { Icon } from "@/components/ui/icon"
import { IconBadge } from "@/components/ui/icon-badge"
import { useProfileSimple } from "@/hooks/use-profile-simple"
import { Skeleton } from "@/components/ui/skeleton"
import { ScrollView, View } from "react-native"
import { User, Mail, Phone, AtSign } from "lucide-react-native"

type InfoFieldProps = {
  label: string
  value?: string
  placeholder: string
  icon: typeof User
  isLoading: boolean
}

function InfoField({ label, value, placeholder, icon, isLoading }: InfoFieldProps) {
  return (
    <View className="gap-1.5">
      <View className="flex-row items-center gap-2">
        <Icon as={icon} className="size-3.5 text-muted-foreground" />
        <Text className="text-xs font-semibold text-muted-foreground uppercase tracking-wider">
          {label}
        </Text>
      </View>
      {isLoading ? (
        <Skeleton className="h-10 w-full rounded-md" />
      ) : (
        <Input
          value={value}
          placeholder={placeholder}
          placeholderTextColor="#71717a"
          editable={false}
        />
      )}
    </View>
  )
}

export default function ProfileInfo() {
  const { data: profile, isLoading } = useProfileSimple()

  return (
    <ScrollView
      className="flex-1"
      contentContainerClassName="px-5 pt-6 pb-10 gap-6"
      showsVerticalScrollIndicator={false}
    >
      <View className="items-center gap-3 pb-2">
        <IconBadge icon={User} variant="muted" size="lg" />
        <Text className="text-sm text-muted-foreground">
          Seus dados pessoais
        </Text>
      </View>

      <Separator />

      <View className="gap-4">
        <InfoField
          label="Nome"
          value={profile?.firstName}
          placeholder="Seu nome"
          icon={User}
          isLoading={isLoading}
        />

        {/* <InfoField
          label="Usuário"
          value={profile?.username}
          placeholder="@usuario"
          icon={AtSign}
          isLoading={isLoading}
        /> */}

        {/* <InfoField
          label="E-mail"
          value={profile?.email}
          placeholder="seuemail@exemplo.com"
          icon={Mail}
          isLoading={isLoading}
        /> */}

        {/* <InfoField
          label="Telefone"
          value={profile?.phone}
          placeholder="(00) 00000-0000"
          icon={Phone}
          isLoading={isLoading}
        /> */}
      </View>
    </ScrollView>
  )
}