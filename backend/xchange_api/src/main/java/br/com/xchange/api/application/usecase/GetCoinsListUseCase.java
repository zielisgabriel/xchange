package br.com.xchange.api.application.usecase;

import java.util.List;

import br.com.xchange.api.domain.entities.CoinWithMarketData;
import br.com.xchange.api.domain.ports.services.CoinServicePort;
import br.com.xchange.api.domain.ports.usecases.GetCoinsListUseCasePort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetCoinsListUseCase implements GetCoinsListUseCasePort {
  private final CoinServicePort coinServicePort;

  @Override
  public List<CoinWithMarketData> execute() {
    return this.coinServicePort.getCoinsWithMarketData();
  }
}
