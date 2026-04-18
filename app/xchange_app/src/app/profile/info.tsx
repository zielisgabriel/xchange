"use client"

import { Input } from "@/components/ui/input";
import { Separator } from "@/components/ui/separator";
import { Text } from "@/components/ui/text";
import { useRef } from "react";
import { View } from "react-native";

export default function ProfileInfo() {
  const firstNameRef = useRef<string>("")
  const emailRef = useRef<string>("")

  return (
    <View className="px-5 pt-8">
      <Text className="text-xs font-semibold text-foreground uppercase tracking-wider mb-1">
        Informações pessoais
      </Text>

      <View>
        <Separator className="my-2" />
      </View>

      <View className="flex gap-2">
        <View className="grid grid-cols-2 gap-2">
          <View className="flex gap-2">
            <Text className="text-xs font-semibold text-muted-foreground uppercase tracking-wider">
              Nome
            </Text>

            <Input
              className="placeholder:text-sm"
              placeholderTextColor={"#FFF"}
              textContentType="name"
              autoComplete="off"
              autoCorrect={false}
              onChangeText={value => firstNameRef.current = value}
            />
          </View>

          <View className="flex gap-2">
            <Text className="text-xs font-semibold text-muted-foreground uppercase tracking-wider">
              Sobrenome
            </Text>

            <Input
              className="placeholder:text-sm"
              placeholderTextColor={"#FFF"}
              textContentType="name"
              autoComplete="off"
              autoCorrect={false}
              onChangeText={value => emailRef.current = value}
            />
          </View>
        </View>

        <Text className="text-xs font-semibold text-muted-foreground uppercase tracking-wider">
          Usuário
        </Text>

        <Input
          className="placeholder:text-sm"
          placeholderTextColor={"#FFF"}
          textContentType="emailAddress"
          autoComplete="off"
          autoCorrect={false}
          onChangeText={value => emailRef.current = value}
        />

        <Text className="text-xs font-semibold text-muted-foreground uppercase tracking-wider">
          Email
        </Text>

        <Input
          className="placeholder:text-sm"
          placeholderTextColor={"#FFF"}
          textContentType="emailAddress"
          autoComplete="off"
          autoCorrect={false}
          onChangeText={value => emailRef.current = value}
        />

        <Text className="text-xs font-semibold text-muted-foreground uppercase tracking-wider">
          Email
        </Text>

        <Input
          className="placeholder:text-sm"
          placeholderTextColor={"#FFF"}
          textContentType="emailAddress"
          autoComplete="off"
          autoCorrect={false}
          onChangeText={value => emailRef.current = value}
        />
      </View>
    </View>
  )
}