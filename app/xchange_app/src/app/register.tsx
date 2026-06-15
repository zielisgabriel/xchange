"use client"

import { Text } from "@/components/ui/text";
import { View } from "react-native";
import { Separator } from "@/components/ui/separator";
import { Link } from "expo-router";
import { RegisterForm } from "@/components/register-form";

export default function Register() {
  return (
    <View className="flex justify-center h-full w-full px-4">
      <View className="mb-4">
        <Text className="text-4xl" variant={"large"}>
          Cadastrar
        </Text>
        <Text className="text-lg" variant={"muted"}>
          uma conta
        </Text>
      </View>

      <RegisterForm />

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
          Já tem uma conta?
        </Text>
        <Link href={"/login"}>
          <Text className="text-sm font-bold text-blue-400 underline">
            Entrar
          </Text>
        </Link>
      </View>
    </View>
  )
}