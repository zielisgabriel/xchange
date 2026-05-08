import { Coin } from "./coin";

export interface CoinWithMarketData extends Coin {
  priceBtc: number,
  marketCap: string,
  totalVolume: string,
  sparkline: string,
  priceChangePercentage24h: number
}