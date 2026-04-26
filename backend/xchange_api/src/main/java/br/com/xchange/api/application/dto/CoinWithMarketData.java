package br.com.xchange.api.application.dto;

import br.com.xchange.api.domain.entities.Coin;

public record CoinWithMarketData(
  Coin coin,
  Double priceBtc,
  String marketCap,
  String totalVolume,
  String sparkline
) {}
