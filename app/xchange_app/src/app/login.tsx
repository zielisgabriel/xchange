import { Button } from "@/components/ui/button";
import { Checkbox } from "@/components/ui/checkbox";
import { Input } from "@/components/ui/input";
import { Text } from "@/components/ui/text";
import { useState } from "react";
import { View } from "react-native";
import GoogleIcon from "@/assets/icons8-google.svg"
import { Separator } from "@/components/ui/separator";
import { Link } from "expo-router";

export default function Login() {
  const [stayConnected, setStayConnected] = useState<boolean>(true);

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

      <View className="space-y-4">
        <Input placeholder="E-mail" className="placeholder:text-sm" />
        <Input placeholder="Senha" className="placeholder:text-sm" />

        <View className="flex flex-row items-center gap-2">
          <Checkbox checked={stayConnected} onCheckedChange={() => setStayConnected(!stayConnected)} />
          <Text>
            Manter conectado
          </Text>
        </View>

        <Button>
          <Text className="font-bold">
            Entrar
          </Text>
        </Button>
      </View>

      <View>
        <Separator className="my-4" />
      </View>

      <View>
        <Button variant={"outline"} className="text-foreground">
          <GoogleIcon width={32} height={32} />
          <Text>
            Entrar com uma conta
          </Text>
        </Button>
      </View>

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