package br.com.xchange.api.application.dto.response;

import java.util.List;

public record CoinsByQueryResponse(
  List<CoinByQuery> coins
) {
  public record CoinByQuery(
    String id,
    String name,
    String symbol,
    String imageUrl
  ) {}
}
