"use client"

import { Text } from "@/components/ui/text";
import { View } from "react-native";
import { Separator } from "@/components/ui/separator";
import { Link } from "expo-router";
import { LoginForm } from "@/components/login-form";

export default function Login() {
  return (
    <View className="flex justify-center h-full w-full px-4">
      <View className="mb-4">
        <Text className="text-4xl" variant={"large"}>
          Entrar
        </Text>
        <Text className="text-lg" variant={"muted"}>
          com uma conta
        </Text>
      </View>

      <LoginForm />

      <View>
        <Separator className="my-4" />
      </View>

      {/* <View>
        <Button variant={"outline"} className="text-foreground">
          <GoogleIcon width={32} height={32} />
          <Text>
            Entrar com uma conta
          </Text>
        </Button>
      </View> */}

      <View className="flex flex-row justify-center gap-1 mt-4">
        <Text className="text-sm">
          Não tem uma conta?
        </Text>
        <Link href={"/register"}>
          <Text className="text-sm font-bold text-blue-400 underline">
            Cadastra-se
          </Text>
        </Link>
      </View>
    </View>
  )
}