package br.com.xchange.api.application.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record CoinsListResponse(
  List<CoinsListWrapper> coins
) {
  public record CoinsListWrapper(
    String id,
    String name,
    String symbol,
    String imageUrl,
    String price,
    BigDecimal priceChangePercentage24h
  ) {}
}
