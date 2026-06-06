package br.com.xchange.api.infra.services.coingecko.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CoinInListWithMarketData(
    String id,
    String symbol,
    String name,
    String image,
    @JsonProperty("current_price") BigDecimal currentPrice,
    @JsonProperty("market_cap") BigDecimal marketCap,
    @JsonProperty("market_cap_rank") Integer marketCapRank,
    @JsonProperty("fully_diluted_valuation") BigDecimal fullyDilutedValuation,
    @JsonProperty("total_volume") BigDecimal totalVolume,
    @JsonProperty("high_24h") BigDecimal high24h,
    @JsonProperty("low_24h") BigDecimal low24h,
    @JsonProperty("price_change_24h") BigDecimal priceChange24h,
    @JsonProperty("price_change_percentage_24h") BigDecimal priceChangePercentage24h,
    @JsonProperty("market_cap_change_24h") BigDecimal marketCapChange24h,
    @JsonProperty("market_cap_change_percentage_24h") BigDecimal marketCapChangePercentage24h,
    @JsonProperty("circulating_supply") BigDecimal circulatingSupply,
    @JsonProperty("total_supply") BigDecimal totalSupply,
    @JsonProperty("max_supply") BigDecimal maxSupply,
    BigDecimal ath,
    @JsonProperty("ath_change_percentage") BigDecimal athChangePercentage,
    @JsonProperty("ath_date") String athDate,
    BigDecimal atl,
    @JsonProperty("atl_change_percentage") BigDecimal atlChangePercentage,
    @JsonProperty("atl_date") String atlDate,
    @JsonProperty("last_updated") String lastUpdated
) {}
