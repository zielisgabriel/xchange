package br.com.xchange.api.application.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record TrendingCoinResponse(
  List<TrendingCoinWrapper> coins
) {
  public record TrendingCoinWrapper(
    String id,
    String name,
    String symbol,
    String imageUrl,
    String price,
    String marketCap,
    String totalVolume,
    BigDecimal priceChangePercentage24h
  ) {};
}
