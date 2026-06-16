"use client"

import { View } from "react-native"
import { Input } from "./ui/input"
import { Text } from "./ui/text"
import { Button } from "./ui/button"
import { useRef, useTransition } from "react"
import { useAuthStore } from "@/hooks/use-auth-store"
import { toast } from "sonner-native"
import { apiFetch } from "@/lib/api-fetch"

export function LoginForm() {
  const emailRef = useRef("")
  const passwordRef = useRef("")
  const { logIn } = useAuthStore()
  const [isPeding, startTransition] = useTransition()

  function handleLogin() {
    startTransition(async () => {
      const email = emailRef.current
      const password = passwordRef.current

      const response = await apiFetch({
        input: "/api/auth/login",
        init: {
          method: "POST",
          body: JSON.stringify({
            email,
            password
          })
        },
        isAuth: false
      })

      const data = await response.json()

      if (data.code == 200) {
        logIn({
          accessToken: data.access_token!,
          refreshToken: data.refresh_token!
        })
        return
      }

      toast.error(data.message)
    })
  }

  return (
    <View className="flex gap-4">
      <Input
        placeholder="E-mail"
        className="placeholder:text-sm"
        placeholderTextColor={"#FFF"}
        textContentType="emailAddress"
        autoComplete="off"
        autoCorrect={false}
        onChangeText={value => emailRef.current = value}
      />
      <Input
        placeholder="Senha"
        className="placeholder:text-sm"
        placeholderTextColor={"#FFF"}
        textContentType="password"
        secureTextEntry={true}
        autoComplete="off"
        autoCorrect={false}
        onChangeText={value => passwordRef.current = value}
      />

      <Button
        onPress={handleLogin}
        disabled={isPeding}
      >
        <Text className="font-bold">
          {isPeding ? "Carregando..." : "Entrar"}
        </Text>
      </Button>
    </View>
  )
}