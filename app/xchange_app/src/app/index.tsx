import { View } from "react-native"
import { GestureHandlerRootView } from "react-native-gesture-handler"
import { Button } from "@/components/ui/button"
import { Separator } from "@/components/ui/separator"
import { useRouter } from "expo-router"
import { Text } from "@/components/ui/text"

export default function Index() {
  const { push } = useRouter();

  return (
    <GestureHandlerRootView className="flex-1">
      <View className="flex-1 bg-background">
        <View className="flex-1 justify-between items-center px-8 pt-24 pb-16">
          <View className="items-center mt-16">

            <Text
              className="text-foreground font-sora-extrabold tracking-widest text-4xl"
            >
              XCHANGE
            </Text>

            <Text
              className="text-muted-foreground font-light text-sm text-center"
            >
              Plataforma para investidores de cripto
            </Text>
          </View>

          <View className="w-full">
            <Button
              variant="default"
              size="lg"
              onPress={() => push("/login")}
            >
              <Text>
                Fazer Login
              </Text>
            </Button>

            <Separator className="my-4" />

            <Button
              variant="secondary"
              size="lg"
            >
              <Text
                className="text-foreground"
              >
                Criar Conta
              </Text>
            </Button>
          </View>

          <Text
            className="text-muted-foreground text-center mt-6 text-[10px]"
          >
            Ao continuar, você concorda com nossos{"\n"}
            <Text className="text-blue-400 text-[10px]">
              <a href="">
                Termos de Uso
              </a>
            </Text>
            {" e "}
            <Text className="text-blue-400 text-[10px]">
              <a href="">
                Política de Privacidade
              </a>
            </Text>
          </Text>
        </View>
      </View>
    </GestureHandlerRootView>
  )
}