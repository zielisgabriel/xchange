interface Coin {
  id: string,
  name: string,
  symbol: string,
  imageUrl: string,
  price: string,
  priceChangePercentage24h: number
}

export interface CoinsListResponse {
  coins: Coin[]
}