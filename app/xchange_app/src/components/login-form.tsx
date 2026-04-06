"use client"

import { View } from "react-native"
import { Input } from "./ui/input"
import { Text } from "./ui/text"
import { Button } from "./ui/button"
import { useRef, useTransition } from "react"
import { loginAction } from "@/actions/login-action"
import { useAuthStore } from "@/hooks/use-auth-store"

export function LoginForm() {
  const emailRef = useRef("")
  const passwordRef = useRef("")
  const { logIn } = useAuthStore()
  const [isPeding, startTransition] = useTransition()

  function handleLogin() {
    startTransition(async () => {
      const email = emailRef.current
      const password = passwordRef.current
      const formData = new FormData()

      formData.append("email", email)
      formData.append("password", password)

      const response = await loginAction(formData)
      if (response.code == 200) {
        logIn(response.access_token!)
        console.log(response)
      }
      console.log(response)
    })
  }

  return (
    <View className="flex gap-4">
      <Input
        placeholder="E-mail"
        className="placeholder:text-sm"
        onChangeText={value => emailRef.current = value}
      />
      <Input
        placeholder="Senha"
        className="placeholder:text-sm"
        onChangeText={value => passwordRef.current = value}
      />

      <Button onPress={handleLogin}>
        <Text className="font-bold">
          Entrar
        </Text>
      </Button>
    </View>
  )
}