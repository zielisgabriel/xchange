package br.com.xchange.api.application.dto.response;

import java.util.List;

public record TrendingCoinResponse(
  List<TrendingCoinWrapper> coins
) {
  public record TrendingCoinWrapper(
    String id,
    String name,
    String symbol,
    String imageUrl,
    Integer price,
    Double priceBtc,
    String marketCap,
    String totalVolume,
    String sparkline
  ) {};
}
