import { FavoriteCoin } from "./favorite-coin";

export interface ProfileSimple {
  id: string,
  firstName: string,
  onboardingFinished: boolean,
  favoriteCoins: FavoriteCoin[]
}