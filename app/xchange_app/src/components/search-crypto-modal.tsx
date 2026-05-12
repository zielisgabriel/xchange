import { useState, useCallback, useEffect, useRef } from "react"
import { Modal, View, TextInput, Pressable, FlatList, ActivityIndicator } from "react-native"
import { Text } from "./ui/text"
import { Skeleton } from "./ui/skeleton"
import { Avatar, AvatarImage } from "./ui/avatar"
import { Icon } from "./ui/icon"
import { Search, X } from "lucide-react-native"
import Animated, { FadeIn, FadeInDown } from "react-native-reanimated"
import { Coin } from "@/types/coin"

const MOCK_COINS: Coin[] = [
  {
    id: "bitcoin",
    name: "Bitcoin",
    symbol: "BTC",
    imageUrl: "https://assets.coingecko.com/coins/images/1/large/bitcoin.png",
    price: 103420.0,
    updatedAt: new Date().toISOString(),
  },
  {
    id: "ethereum",
    name: "Ethereum",
    symbol: "ETH",
    imageUrl: "https://assets.coingecko.com/coins/images/279/large/ethereum.png",
    price: 2487.35,
    updatedAt: new Date().toISOString(),
  },
  {
    id: "solana",
    name: "Solana",
    symbol: "SOL",
    imageUrl:
      "https://assets.coingecko.com/coins/images/4128/large/solana.png",
    price: 174.62,
    updatedAt: new Date().toISOString(),
  },
  {
    id: "binancecoin",
    name: "BNB",
    symbol: "BNB",
    imageUrl:
      "https://assets.coingecko.com/coins/images/825/large/bnb-icon2_2x.png",
    price: 649.1,
    updatedAt: new Date().toISOString(),
  },
  {
    id: "ripple",
    name: "XRP",
    symbol: "XRP",
    imageUrl: "https://assets.coingecko.com/coins/images/44/large/xrp-symbol-white-128.png",
    price: 2.42,
    updatedAt: new Date().toISOString(),
  },
  {
    id: "cardano",
    name: "Cardano",
    symbol: "ADA",
    imageUrl:
      "https://assets.coingecko.com/coins/images/975/large/cardano.png",
    price: 0.81,
    updatedAt: new Date().toISOString(),
  },
  {
    id: "dogecoin",
    name: "Dogecoin",
    symbol: "DOGE",
    imageUrl:
      "https://assets.coingecko.com/coins/images/5/large/dogecoin.png",
    price: 0.228,
    updatedAt: new Date().toISOString(),
  },
  {
    id: "avalanche-2",
    name: "Avalanche",
    symbol: "AVAX",
    imageUrl:
      "https://assets.coingecko.com/coins/images/12559/large/Avalanche_Circle_RedWhite_Trans.png",
    price: 25.03,
    updatedAt: new Date().toISOString(),
  },
  {
    id: "polkadot",
    name: "Polkadot",
    symbol: "DOT",
    imageUrl:
      "https://assets.coingecko.com/coins/images/12171/large/polkadot.png",
    price: 5.12,
    updatedAt: new Date().toISOString(),
  },
  {
    id: "chainlink",
    name: "Chainlink",
    symbol: "LINK",
    imageUrl:
      "https://assets.coingecko.com/coins/images/877/large/chainlink-new-logo.png",
    price: 16.88,
    updatedAt: new Date().toISOString(),
  },
]

const currencyFormat = new Intl.NumberFormat("en-US", {
  currency: "USD",
  style: "currency",
  maximumFractionDigits: 7,
})

const MAX_RESULTS = 5
const MIN_QUERY_LENGTH = 3
const DEBOUNCE_MS = 2000

interface SearchCryptoModalProps {
  visible: boolean
  onClose: () => void
}

function CoinRow({ coin }: { coin: Coin }) {
  return (
    <Pressable className="flex-row items-center justify-between px-4 py-3.5 active:bg-muted/50 rounded-2xl">
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
      <Text className="text-sm font-medium">
        {currencyFormat.format(coin.price)}
      </Text>
    </Pressable>
  )
}

export function SearchCryptoModal({ visible, onClose }: SearchCryptoModalProps) {
  const [query, setQuery] = useState("")
  const [debouncedQuery, setDebouncedQuery] = useState("")
  const [isLoading, setIsLoading] = useState(false)
  const timerRef = useRef<ReturnType<typeof setTimeout> | null>(null)

  useEffect(() => {
    if (query.length < MIN_QUERY_LENGTH) {
      setDebouncedQuery("")
      setIsLoading(false)
      if (timerRef.current) clearTimeout(timerRef.current)
      return
    }

    setIsLoading(true)
    timerRef.current = setTimeout(() => {
      setDebouncedQuery(query)
      setIsLoading(false)
    }, DEBOUNCE_MS)

    return () => {
      if (timerRef.current) clearTimeout(timerRef.current)
    }
  }, [query])

  const filtered = debouncedQuery.length >= MIN_QUERY_LENGTH
    ? MOCK_COINS.filter(
        (coin) =>
          coin.name.toLowerCase().includes(debouncedQuery.toLowerCase()) ||
          coin.symbol.toLowerCase().includes(debouncedQuery.toLowerCase())
      ).slice(0, MAX_RESULTS)
    : []

  const handleClose = useCallback(() => {
    setQuery("")
    setDebouncedQuery("")
    setIsLoading(false)
    if (timerRef.current) clearTimeout(timerRef.current)
    onClose()
  }, [onClose])

  const renderItem = useCallback(
    ({ item, index }: { item: Coin; index: number }) => (
      <Animated.View
        entering={FadeInDown.delay(index * 40).duration(300).springify().damping(18)}
      >
        <CoinRow coin={item} />
      </Animated.View>
    ),
    []
  )

  const keyExtractor = useCallback((item: Coin) => item.id, [])

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
        </Animated.View>

        <View className="h-px bg-border/50 mx-4" />

        {query.length < MIN_QUERY_LENGTH ? (
          <Animated.View
            entering={FadeIn.duration(400)}
            className="flex-1 items-center justify-center px-8 gap-2"
          >
            <Icon as={Search} className="size-10 text-muted-foreground/30" />
            <Text className="text-sm text-muted-foreground/50 text-center">
              Digite pelo menos {MIN_QUERY_LENGTH} caracteres para buscar
            </Text>
          </Animated.View>
        ) : isLoading ? (
          <View className="px-2 pt-2 gap-1">
            {Array.from({ length: MAX_RESULTS }).map((_, i) => (
              <View key={i} className="flex-row items-center justify-between px-4 py-3.5">
                <View className="flex-row items-center gap-3">
                  <Skeleton className="w-9 h-9 rounded-full" />
                  <View className="gap-1.5">
                    <Skeleton className="w-20 h-4 rounded" />
                    <Skeleton className="w-10 h-3 rounded" />
                  </View>
                </View>
                <Skeleton className="w-24 h-4 rounded" />
              </View>
            ))}
          </View>
        ) : filtered.length === 0 ? (
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
        ) : (
          <FlatList
            data={filtered}
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
