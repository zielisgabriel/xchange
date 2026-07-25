package br.com.xchange.api.application.usecase;

import java.util.List;

import br.com.xchange.api.domain.entities.Coin;
import br.com.xchange.api.domain.ports.services.CoinServicePort;
import br.com.xchange.api.domain.ports.usecases.GetSimpleCoinUseCasePort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetSimpleCoinUseCase implements GetSimpleCoinUseCasePort {
  private final CoinServicePort coinServicePort;

  // TODO: Criar um método próprio para simple coins.
  @Override
  public List<Coin> execute() {
    return this.coinServicePort.getCoinsWithMarketData()
      .stream()
      .map(coin -> {
        Coin simpleCoin = new Coin();
        simpleCoin.setId(coin.getId());
        simpleCoin.setImageUrl(coin.getImageUrl());
        simpleCoin.setName(coin.getName());
        simpleCoin.setPrice(coin.getPrice());
        simpleCoin.setSymbol(coin.getSymbol());
        simpleCoin.setUpdatedAt(coin.getUpdatedAt());

        return simpleCoin;
      }).toList();
  }
}
