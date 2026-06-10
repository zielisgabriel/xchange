package br.com.xchange.api.infra.services.coingecko.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CoinDetail(
    String id,
    String symbol,
    String name,
    ImageData image,
    Description description,
    @JsonProperty("hashing_algorithm") String hashingAlgorithm,
    @JsonProperty("genesis_date") String genesisDate,
    @JsonProperty("sentiment_votes_up_percentage") BigDecimal sentimentVotesUpPercentage,
    @JsonProperty("sentiment_votes_down_percentage") BigDecimal sentimentVotesDownPercentage,
    @JsonProperty("watchlist_portfolio_users") Long watchlistPortfolioUsers,
    @JsonProperty("market_cap_rank") Integer marketCapRank,
    @JsonProperty("market_data") MarketData marketData,
    @JsonProperty("last_updated") String lastUpdated
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ImageData(
        String thumb,
        String small,
        String large
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Description(
        String en
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record MarketData(
        // Mapas de moeda — use .get("usd") para extrair o valor
        @JsonProperty("current_price") Map<String, BigDecimal> currentPrice,
        @JsonProperty("market_cap") Map<String, BigDecimal> marketCap,
        @JsonProperty("fully_diluted_valuation") Map<String, BigDecimal> fullyDilutedValuation,
        @JsonProperty("total_volume") Map<String, BigDecimal> totalVolume,
        @JsonProperty("high_24h") Map<String, BigDecimal> high24h,
        @JsonProperty("low_24h") Map<String, BigDecimal> low24h,
        @JsonProperty("ath") Map<String, BigDecimal> ath,
        @JsonProperty("ath_change_percentage") Map<String, BigDecimal> athChangePercentage,
        @JsonProperty("ath_date") Map<String, String> athDate,
        @JsonProperty("atl") Map<String, BigDecimal> atl,
        @JsonProperty("atl_change_percentage") Map<String, BigDecimal> atlChangePercentage,
        @JsonProperty("atl_date") Map<String, String> atlDate,

        // 1h só existe no mapa por moeda
        @JsonProperty("price_change_percentage_1h_in_currency") Map<String, BigDecimal> priceChangePercentage1hInCurrency,

        // Escalares — já são independentes de moeda no response da CoinGecko
        @JsonProperty("price_change_24h") BigDecimal priceChange24h,
        @JsonProperty("price_change_percentage_24h") BigDecimal priceChangePercentage24h,
        @JsonProperty("price_change_percentage_7d") BigDecimal priceChangePercentage7d,
        @JsonProperty("price_change_percentage_14d") BigDecimal priceChangePercentage14d,
        @JsonProperty("price_change_percentage_30d") BigDecimal priceChangePercentage30d,
        @JsonProperty("price_change_percentage_60d") BigDecimal priceChangePercentage60d,
        @JsonProperty("price_change_percentage_200d") BigDecimal priceChangePercentage200d,
        @JsonProperty("price_change_percentage_1y") BigDecimal priceChangePercentage1y,
        @JsonProperty("market_cap_change_24h") BigDecimal marketCapChange24h,
        @JsonProperty("market_cap_change_percentage_24h") BigDecimal marketCapChangePercentage24h,
        @JsonProperty("market_cap_fdv_ratio") BigDecimal marketCapFdvRatio,
        @JsonProperty("circulating_supply") BigDecimal circulatingSupply,
        @JsonProperty("total_supply") BigDecimal totalSupply,
        @JsonProperty("max_supply") BigDecimal maxSupply,

        @JsonProperty("sparkline_7d") SparklineData sparkline7d,
        @JsonProperty("last_updated") String lastUpdated
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record SparklineData(
        List<Double> price
    ) {}
}