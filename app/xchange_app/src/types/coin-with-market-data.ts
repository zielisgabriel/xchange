import { Coin } from "./coin";

export interface CoinWithMarketData extends Coin {
  priceBtc: number,
  marketCap: string,
  totalVolume: string,
  priceChangePercentage24h: number
}