package br.com.xchange.api.application.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record CoinDetailResponse(
    String id,
    String name,
    String symbol,
    String imageUrl,
    String description,
    String hashingAlgorithm,
    String genesisDate,
    Integer marketCapRank,
    Long watchlistPortfolioUsers,
    BigDecimal sentimentVotesUpPercentage,
    BigDecimal sentimentVotesDownPercentage,

    String price,
    String high24h,
    String low24h,
    String priceChange24h,
    String marketCap,
    String totalVolume,
    String fullyDilutedValuation,

    BigDecimal priceChangePercentage1h,
    BigDecimal priceChangePercentage24h,
    BigDecimal priceChangePercentage7d,
    BigDecimal priceChangePercentage14d,
    BigDecimal priceChangePercentage30d,
    BigDecimal priceChangePercentage60d,
    BigDecimal priceChangePercentage200d,
    BigDecimal priceChangePercentage1y,

    BigDecimal circulatingSupply,
    BigDecimal totalSupply,
    BigDecimal maxSupply,

    String ath,
    BigDecimal athChangePercentage,
    String athDate,
    String atl,
    BigDecimal atlChangePercentage,
    String atlDate,

    List<Double> sparkline7d,
    String updatedAt
) {}