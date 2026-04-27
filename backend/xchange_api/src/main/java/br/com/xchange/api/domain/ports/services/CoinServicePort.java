package br.com.xchange.api.domain.ports.services;

import java.util.List;

import br.com.xchange.api.domain.entities.Coin;
import br.com.xchange.api.domain.entities.CoinWithMarketData;

public interface CoinServicePort {
  List<Coin> getTrendingCoins();
  List<CoinWithMarketData> getTrendingCoinsDetailed();
}
