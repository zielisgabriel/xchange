package br.com.xchange.api.domain.ports.usecases;

import java.util.List;

import br.com.xchange.api.domain.entities.CoinWithMarketData;

public interface GetCoinsListUseCasePort {
  List<CoinWithMarketData> execute();
}
