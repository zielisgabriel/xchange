package br.com.xchange.api.domain.ports.services;

import java.util.List;

import br.com.xchange.api.application.dto.CoinWithMarketData;
import br.com.xchange.api.domain.entities.Coin;

public interface CoinServicePort {
  List<Coin> getTrendingCoins();
  List<CoinWithMarketData> getTrendingCoinsDetailed();
}
