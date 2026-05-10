package br.com.xchange.api.infra.services.coingecko.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GlobalCoinMetrics(
  Data data
) {
  @JsonIgnoreProperties(ignoreUnknown = true)
  public record Data(
    @JsonProperty(value = "total_market_cap")
    TotalMarketCap totalMarketCap,
    
    @JsonProperty(value = "total_volume")
    TotalVolume totalVolume,

    @JsonProperty(value = "market_cap_change_percentage_24h_usd")
    BigDecimal marketCapChangePercentage24hUsd,

    @JsonProperty(value = "volume_change_percentage_24h_usd")
    BigDecimal volumeChangePercentage24hUsd
  ) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record TotalMarketCap(
      BigDecimal usd
    ) {}
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record TotalVolume(
      BigDecimal usd
    ) {}
  }
}
