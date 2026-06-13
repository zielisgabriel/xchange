package br.com.xchange.api.infra.services.coingecko.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CoinsByQuery(
  List<CoinByQuery> coins
) {
  public record CoinByQuery(
    String id,
    String name,
    String symbol,
    String thumb
  ) {}
}
