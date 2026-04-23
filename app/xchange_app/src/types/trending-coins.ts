export type TrendingCoins = {
  coins: Coin[]
}

type Coin = {
  item: ItemCoin
}

export type ItemCoin = {
  id: string;
  coin_id: number;
  name: string;
  symbol: string;
  market_cap_rank: number;
  thumb: string;
  small: string;
  large: string;
  slug: string;
  price_btc: number;
  score: number;
  data: DataItemCoin;
  market_cap: string;
  market_cap_btc: string;
  total_volume: string;
  total_volume_btc: string;
  sparkline: string;
  content?: any;
}

export type DataItemCoin = {
  price: number;
  price_btc: number;
  price_change_percentage_24h: Record<string, number>;
};