package br.com.xchange.api.infra.services.coingecko.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TrendingCoinsCoinsGeckoResponse(
  List<CoinWrapper> coins
) {

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record CoinWrapper(
    CoinItem item
  ) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record CoinItem(
    String id,
    @JsonProperty("coin_id") Integer coinId,
    String name,
    String symbol,
    @JsonProperty("market_cap_rank") Integer marketCapRank,
    String thumb,
    String small,
    String large,
    String slug,
    @JsonProperty("price_btc") BigDecimal priceBtc,
    Integer score,
    CoinData data
  ) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record CoinData(
    BigDecimal price,
    @JsonProperty("price_btc") String priceBtcStr,
    @JsonProperty("market_cap") String marketCap,
    @JsonProperty("market_cap_btc") String marketCapBtc,
    @JsonProperty("total_volume") String totalVolume,
    @JsonProperty("total_volume_btc") String totalVolumeBtc,
    String sparkline,
    CoinContent content
  ) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record CoinContent(
    String title,
    String description
  ) {}
}
