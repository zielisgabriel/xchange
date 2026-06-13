import { View, Pressable, ScrollView, Image } from "react-native"
import { Text } from "@/components/ui/text"
import { Icon } from "@/components/ui/icon"
import { Button } from "@/components/ui/button"
import { Star, Check, X, AlertCircle, Search } from "lucide-react-native"
import { useState, useMemo, useEffect } from "react"
import Animated, { FadeInDown } from "react-native-reanimated"
import { SimpleCoin } from "@/types/simple-coin"
import { FavoriteCoin } from "@/types/favorite-coin"
import { apiFetch } from "@/lib/api-fetch"
import { SearchCryptoModal } from "@/components/search-crypto-modal"

const MAX_FAVORITES = 5

interface StepCryptosProps {
  selected: FavoriteCoin[]
  onToggle: (coin: FavoriteCoin) => void
  error: string | null
}

function SelectedCoinChip({ coin, onRemove }: { coin: FavoriteCoin; onRemove: () => void }) {
  return (
    <Pressable
      onPress={onRemove}
      className="flex-row items-center gap-1.5 bg-amber-500/15 border border-amber-500/20 rounded-full px-3 py-1.5"
    >
      <Text className="text-amber-300 text-xs font-sora-semibold uppercase">
        {coin.symbol}
      </Text>
      <Icon as={X} className="size-3 text-amber-300/60" />
    </Pressable>
  )
}

function CoinCard({ coin, isSelected, isDisabled, onPress }: {
  coin: SimpleCoin
  isSelected: boolean
  isDisabled: boolean
  onPress: () => void
}) {
  const containerStyle = isSelected
    ? "bg-white/10 border-white/20"
    : isDisabled
      ? "bg-white/2 border-white/5 opacity-40"
      : "bg-white/5 border-white/8 active:bg-white/10"

  return (
    <Pressable
      onPress={onPress}
      disabled={isDisabled}
      className={`flex-row items-center gap-2 rounded-2xl px-3 py-2 border ${containerStyle}`}
    >
      <Image source={{ uri: coin.imageUrl }} className="w-6 h-6 rounded-full bg-white/10" />
      <View>
        <Text
          className={`text-xs font-sora-semibold ${isSelected ? "text-foreground" : "text-foreground/70"}`}
        >
          {coin.name}
        </Text>
        <Text className="text-[10px] text-foreground/30 uppercase">
          {coin.symbol}
        </Text>
      </View>
      {isSelected && (
        <View className="w-4 h-4 rounded-full bg-emerald-500 items-center justify-center ml-1">
          <Icon as={Check} className="size-2.5 text-white" />
        </View>
      )}
    </Pressable>
  )
}

export function StepCryptos({ selected, onToggle, error }: StepCryptosProps) {
  const [simpleCoins, setSimpleCoins] = useState<SimpleCoin[]>([])
  const [searchVisible, setSearchVisible] = useState(false)

  const selectedIds = useMemo(
    () => new Set(selected.map((c) => c.coinId)),
    [selected]
  )

  useEffect(() => {
    fetchSimpleCoins()
  }, [])

  async function fetchSimpleCoins() {
    try {
      const response = await apiFetch({
        input: "/api/coins/simple",
        init: { method: "GET" },
      })

      const data = await response.json()
      const coins: SimpleCoin[] = data?.coins ?? []
      setSimpleCoins(coins)

      if (selected.length === 0 && coins.length > 0) {
        onToggle(toFavoriteCoin(coins[0]))
      }
    } catch (error) {
      console.error(error)
      setSimpleCoins([])
    }
  }

  function toFavoriteCoin(coin: SimpleCoin): FavoriteCoin {
    return { coinId: coin.id, name: coin.name, symbol: coin.symbol, imageUrl: coin.imageUrl }
  }

  return (
    <View className="flex-1 px-6 pt-16 pb-6">
      <Animated.View entering={FadeInDown.delay(100).duration(500)}>
        <View className="flex-row items-center gap-2 mb-2">
          <Icon as={Star} className="size-5 text-amber-400" />
          <Text className="text-foreground font-sora-bold text-xl">
            Suas favoritas
          </Text>
        </View>
        <Text className="text-foreground/40 text-sm mb-3">
          Escolha até {MAX_FAVORITES} criptos para acompanhar de perto
        </Text>
        <Button
          onPress={() => setSearchVisible(true)}
          variant="outline"
          className="flex-row items-center gap-2 mb-2"
        >
          <Icon as={Search} className="size-4 text-foreground/60" />
          <Text className="text-sm">Pesquisar</Text>
        </Button>
      </Animated.View>

      {error && (
        <Animated.View
          entering={FadeInDown.duration(300)}
          className="mx-0 mb-4 flex-row items-center gap-2.5 bg-red-500/10 border border-red-500/20 rounded-2xl px-4 py-3"
        >
          <Icon as={AlertCircle} className="size-4 text-red-400" />
          <Text className="text-red-300 text-xs font-sora-medium flex-1">
            {error}
          </Text>
        </Animated.View>
      )}

      <Animated.View entering={FadeInDown.delay(200).duration(500)}>
        {selected.length > 0 && (
          <View className="mb-4">
            <Text className="text-foreground/30 text-[10px] uppercase tracking-widest mb-2 font-sora-semibold">
              Selecionadas ({selected.length}/{MAX_FAVORITES})
            </Text>
            <View className="flex-row flex-wrap gap-2">
              {selected.map((coin) => (
                <SelectedCoinChip
                  key={coin.coinId}
                  coin={coin}
                  onRemove={() => onToggle(coin)}
                />
              ))}
            </View>
          </View>
        )}
      </Animated.View>

      <ScrollView
        showsVerticalScrollIndicator={false}
        className="flex-1"
        contentContainerClassName="pb-4"
      >
        <View className="flex-row flex-wrap justify-center gap-2">
          {simpleCoins.map((coin) => {
            const isSelected = selectedIds.has(coin.id)
            const isDisabled = !isSelected && selected.length >= MAX_FAVORITES

            return (
              <CoinCard
                key={coin.id}
                coin={coin}
                isSelected={isSelected}
                isDisabled={isDisabled}
                onPress={() => onToggle(toFavoriteCoin(coin))}
              />
            )
          })}
        </View>
      </ScrollView>

      <SearchCryptoModal
        visible={searchVisible}
        onClose={() => setSearchVisible(false)}
        onSelect={(coin) => {
          if (selected.length < MAX_FAVORITES && !selectedIds.has(coin.id)) {
            onToggle({ coinId: coin.id, name: coin.name, symbol: coin.symbol, imageUrl: coin.imageUrl })
          }
          setSearchVisible(false)
        }}
      />
    </View>
  )
}
