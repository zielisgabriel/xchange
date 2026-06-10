import { FavoriteCoin } from "./favorite-coin";

interface FavoriteCoinWithPrediction extends FavoriteCoin {
  prediction: string
}

export type FavoriteCoinsWithPredictionResponse = FavoriteCoinWithPrediction[]