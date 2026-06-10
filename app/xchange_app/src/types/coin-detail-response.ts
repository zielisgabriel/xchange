export interface CoinDetailResponse {
  id: string
  name: string
  symbol: string
  imageUrl: string
  description: string
  hashingAlgorithm: string
  genesisDate: string
  marketCapRank: number
  watchlistPortfolioUsers: number
  sentimentVotesUpPercentage: number
  sentimentVotesDownPercentage: number

  price: string
  high24h: string
  low24h: string
  priceChange24h: string
  marketCap: string
  totalVolume: string
  fullyDilutedValuation: string

  priceChangePercentage1h: number
  priceChangePercentage24h: number
  priceChangePercentage7d: number
  priceChangePercentage14d: number
  priceChangePercentage30d: number
  priceChangePercentage60d: number
  priceChangePercentage200d: number
  priceChangePercentage1y: number

  circulatingSupply: number
  totalSupply: number
  maxSupply: number

  ath: string
  athChangePercentage: number
  athDate: string
  atl: string
  atlChangePercentage: number
  atlDate: string

  sparkline7d: number[]
  updatedAt: string
}
