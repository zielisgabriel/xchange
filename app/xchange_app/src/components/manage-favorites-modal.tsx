import { Modal, View, Pressable, ScrollView } from "react-native"
import { Text } from "./ui/text"
import { Avatar, AvatarFallback, AvatarImage } from "./ui/avatar"
import { Icon } from "./ui/icon"
import { Repeat, Star, X } from "lucide-react-native"
import Animated, { FadeIn, FadeInDown } from "react-native-reanimated"
import { FavoriteCoin } from "@/types/favorite-coin"

interface ManageFavoritesModalProps {
  visible: boolean
  newCoin: FavoriteCoin | null
  favorites: FavoriteCoin[]
  onClose: () => void
  onSwap: (favoriteCoinIdToRemove: string) => void
  isSwapping?: boolean
}

function CoinAvatar({ coin, size }: { coin: FavoriteCoin; size: string }) {
  return (
    <Avatar alt={coin.name} className={size}>
      {coin.imageUrl ? (
        <AvatarImage source={{ uri: coin.imageUrl }} />
      ) : (
        <AvatarFallback>
          <Text className="text-xs font-semibold">
            {coin.symbol?.slice(0, 2).toUpperCase() ?? "?"}
          </Text>
        </AvatarFallback>
      )}
    </Avatar>
  )
}

export function ManageFavoritesModal({
  visible,
  newCoin,
  favorites,
  onClose,
  onSwap,
  isSwapping = false,
}: ManageFavoritesModalProps) {
  return (
    <Modal
      visible={visible}
      animationType="slide"
      presentationStyle="formSheet"
      onRequestClose={onClose}
    >
      <View className="flex-1 bg-background">
        <View className="flex-row items-center justify-between px-5 pt-5 pb-3">
          <View className="flex-row items-center gap-2">
            <Icon as={Star} className="size-5 text-amber-400" />
            <Text className="text-lg font-bold">Limite atingido</Text>
          </View>
          <Pressable onPress={onClose} hitSlop={8} disabled={isSwapping}>
            <Icon as={X} className="size-5 text-muted-foreground" />
          </Pressable>
        </View>

        <Text className="px-5 text-sm text-muted-foreground leading-5">
          Você já tem 5 moedas favoritas. Toque em uma para substituí-la por{" "}
          <Text className="text-sm font-semibold text-foreground">
            {newCoin?.name ?? "esta moeda"}
          </Text>
          .
        </Text>

        {newCoin ? (
          <Animated.View
            entering={FadeIn.duration(300)}
            className="mx-5 mt-4 flex-row items-center gap-3 rounded-2xl border border-primary/30 bg-primary/5 px-4 py-3"
          >
            <CoinAvatar coin={newCoin} size="w-10 h-10" />
            <View className="flex-1">
              <Text className="text-sm font-semibold">{newCoin.name}</Text>
              <Text className="text-xs text-muted-foreground uppercase">{newCoin.symbol}</Text>
            </View>
            <Text className="text-[10px] font-semibold uppercase tracking-wider text-primary">
              vai entrar
            </Text>
          </Animated.View>
        ) : null}

        <Text className="px-5 mt-5 mb-1 text-[10px] uppercase tracking-widest text-muted-foreground/60 font-semibold">
          Suas favoritas
        </Text>

        <ScrollView contentContainerClassName="px-3 pb-8" showsVerticalScrollIndicator={false}>
          {favorites.map((coin, index) => (
            <Animated.View
              key={coin.coinId}
              entering={FadeInDown.delay(index * 40).duration(300)}
            >
              <Pressable
                onPress={() => onSwap(coin.coinId)}
                disabled={isSwapping}
                className="flex-row items-center gap-3 rounded-2xl px-3 py-3 active:bg-muted/50"
                style={{ opacity: isSwapping ? 0.5 : 1 }}
              >
                <CoinAvatar coin={coin} size="w-10 h-10" />
                <View className="flex-1">
                  <Text className="text-sm font-semibold">{coin.name}</Text>
                  <Text className="text-xs text-muted-foreground uppercase">{coin.symbol}</Text>
                </View>
                <View className="flex-row items-center gap-1">
                  <Icon as={Repeat} className="size-4 text-muted-foreground" />
                  <Text className="text-xs text-muted-foreground font-medium">Substituir</Text>
                </View>
              </Pressable>
            </Animated.View>
          ))}
        </ScrollView>
      </View>
    </Modal>
  )
}
