package br.com.xchange.api.infra.services.dto.coingecko;

import java.util.List;

public record TrendingCoinsCoinsGeckoResponse(
  List<Coin> coins
) {}
