import { useState, useCallback, useTransition } from "react"
import { Modal, View, TextInput, Pressable, FlatList } from "react-native"
import { Text } from "./ui/text"
import { Skeleton } from "./ui/skeleton"
import { Avatar, AvatarImage } from "./ui/avatar"
import { Icon } from "./ui/icon"
import { Button } from "./ui/button"
import { Search, X } from "lucide-react-native"
import Animated, { FadeIn, FadeInDown } from "react-native-reanimated"
import { apiFetch } from "@/lib/api-fetch"
import { router } from "expo-router"

const MAX_RESULTS = 20

interface SearchCoin {
  id: string
  name: string
  symbol: string
  imageUrl: string
}

interface SearchCryptoModalProps {
  visible: boolean
  onClose: () => void
  onSelect?: (coin: SearchCoin) => void
}

function CoinRow({ coin, onPress }: { coin: SearchCoin; onPress?: () => void }) {
  return (
    <Pressable
      onPress={onPress}
      className="flex-row items-center justify-between px-4 py-3.5 active:bg-muted/50 rounded-2xl"
    >
      <View className="flex-row items-center gap-3">
        <Avatar alt={coin.name} className="w-9 h-9">
          <AvatarImage source={{ uri: coin.imageUrl }} />
        </Avatar>
        <View>
          <Text className="font-semibold text-sm">{coin.name}</Text>
          <Text className="text-xs text-muted-foreground uppercase tracking-wide">
            {coin.symbol}
          </Text>
        </View>
      </View>
    </Pressable>
  )
}

export function SearchCryptoModal({ visible, onClose, onSelect }: SearchCryptoModalProps) {
  const [query, setQuery] = useState("")
  const [coinsSearched, setCoinsSearched] = useState<SearchCoin[]>([])
  const [hasSearched, setHasSearched] = useState(false)
  const [isPending, startTransition] = useTransition()

  function handleSearch() {
    if (query.trim().length === 0) return

    setHasSearched(true)
    startTransition(async () => {
      const response = await apiFetch({
        input: `/api/coins/search?query=${encodeURIComponent(query.trim())}`,
        init: {
          method: "GET"
        }
      })

      if (!response.ok) return setCoinsSearched([])

      const data = await response.json()
      setCoinsSearched(data.coins ?? [])
    })
  }

  const handleClose = useCallback(() => {
    setQuery("")
    setCoinsSearched([])
    setHasSearched(false)
    onClose()
  }, [onClose])

  const renderItem = useCallback(
    ({ item, index }: { item: SearchCoin; index: number }) => (
      <Animated.View
        entering={FadeInDown.delay(index * 40).duration(300).springify().damping(18)}
      >
        <CoinRow
          coin={item}
          onPress={() => {
            if (onSelect) {
              onSelect(item)
            } else {
              handleClose()
              router.push(`/coin/${item.id}`)
            }
          }}
        />
      </Animated.View>
    ),
    [onSelect, handleClose]
  )

  const keyExtractor = useCallback((item: SearchCoin) => item.id, [])

  return (
    <Modal
      visible={visible}
      presentationStyle="formSheet"
      animationType="slide"
      onRequestClose={handleClose}
    >
      <View className="flex-1 bg-background">
        <Animated.View entering={FadeIn.duration(300)} className="px-4 pt-4 pb-3">
          <View className="flex-row items-center gap-3">
            <View className="flex-1 flex-row items-center gap-2.5 bg-muted/60 rounded-2xl px-4 py-3">
              <Icon as={Search} className="size-4 text-muted-foreground" />
              <TextInput
                className="flex-1 text-sm text-foreground"
                placeholder="Buscar criptomoedas..."
                placeholderTextColor="rgba(255,255,255,0.3)"
                value={query}
                onChangeText={setQuery}
                autoFocus
                returnKeyType="search"
                onSubmitEditing={handleSearch}
                autoCapitalize="none"
                autoCorrect={false}
              />
              {query.length > 0 && (
                <Pressable onPress={() => setQuery("")} hitSlop={8}>
                  <Icon as={X} className="size-4 text-muted-foreground" />
                </Pressable>
              )}
            </View>
            <Pressable onPress={handleClose} hitSlop={8}>
              <Text className="text-sm text-primary font-medium">Cancelar</Text>
            </Pressable>
          </View>

          <Button
            onPress={handleSearch}
            disabled={query.trim().length === 0 || isPending}
            className="mt-3"
          >
            <Text>{isPending ? "Buscando..." : "Pesquisar"}</Text>
          </Button>
        </Animated.View>

        <View className="h-px bg-border/50 mx-4" />

        {isPending ? (
          <View className="px-2 pt-2 gap-1">
            {Array.from({ length: 5 }).map((_, i) => (
              <View key={i} className="flex-row items-center justify-between px-4 py-3.5">
                <View className="flex-row items-center gap-3">
                  <Skeleton className="w-9 h-9 rounded-full" />
                  <View className="gap-1.5">
                    <Skeleton className="w-20 h-4 rounded" />
                    <Skeleton className="w-10 h-3 rounded" />
                  </View>
                </View>
              </View>
            ))}
          </View>
        ) : hasSearched && coinsSearched.length === 0 ? (
          <Animated.View
            entering={FadeIn.duration(400)}
            className="flex-1 items-center justify-center px-8 gap-2"
          >
            <Text className="text-sm text-muted-foreground text-center">
              Nenhuma criptomoeda encontrada
            </Text>
            <Text className="text-xs text-muted-foreground/50 text-center">
              Tente buscar por outro termo
            </Text>
          </Animated.View>
        ) : !hasSearched ? (
          <Animated.View
            entering={FadeIn.duration(400)}
            className="flex-1 items-center justify-center px-8 gap-2"
          >
            <Icon as={Search} className="size-10 text-muted-foreground/30" />
            <Text className="text-sm text-muted-foreground/50 text-center">
              Digite o nome de uma criptomoeda e clique em Pesquisar
            </Text>
          </Animated.View>
        ) : (
          <FlatList
            data={coinsSearched}
            renderItem={renderItem}
            keyExtractor={keyExtractor}
            contentContainerClassName="px-2 pt-2"
            keyboardShouldPersistTaps="handled"
            showsVerticalScrollIndicator={false}
          />
        )}
      </View>
    </Modal>
  )
}
