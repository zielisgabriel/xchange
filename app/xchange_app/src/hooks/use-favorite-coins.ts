import { apiFetch } from "@/lib/api-fetch"
import { FavoriteCoin } from "@/types/favorite-coin"
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query"

export const FAVORITE_COINS_LIST_KEY = ["favorite-coins", "list"] as const

async function fetchFavoriteCoins(): Promise<FavoriteCoin[]> {
  const response = await apiFetch({
    input: "/api/profile/favorite-coins",
    init: { method: "GET" },
  })

  if (!response.ok) return []

  return (await response.json()) as FavoriteCoin[]
}

export function useFavoriteCoins() {
  const queryClient = useQueryClient()

  const query = useQuery<FavoriteCoin[]>({
    queryKey: FAVORITE_COINS_LIST_KEY,
    queryFn: fetchFavoriteCoins,
  })

  function invalidateFavorites() {
    queryClient.invalidateQueries({ queryKey: ["favorite-coins"] })
  }

  const addFavorite = useMutation({
    mutationFn: async (coin: FavoriteCoin) => {
      const response = await apiFetch({
        input: "/api/profile/favorite-coins",
        init: { method: "POST", body: JSON.stringify(coin) },
      })

      if (!response.ok) {
        const data = await response.json().catch(() => null)
        throw new Error(data?.message ?? "Não foi possível adicionar aos favoritos")
      }

      return (await response.json()) as FavoriteCoin[]
    },
    onSuccess: invalidateFavorites,
  })

  const removeFavorite = useMutation({
    mutationFn: async (coinId: string) => {
      const response = await apiFetch({
        input: `/api/profile/favorite-coins/${coinId}`,
        init: { method: "DELETE" },
      })

      if (!response.ok) {
        const data = await response.json().catch(() => null)
        throw new Error(data?.message ?? "Não foi possível remover dos favoritos")
      }

      return (await response.json()) as FavoriteCoin[]
    },
    onSuccess: invalidateFavorites,
  })

  const favorites = query.data ?? []
  const isFull = favorites.length >= 5

  function isFavorite(coinId: string): boolean {
    return favorites.some((coin) => coin.coinId === coinId)
  }

  return {
    favorites,
    isLoading: query.isLoading,
    isFull,
    isFavorite,
    addFavorite,
    removeFavorite,
  }
}
