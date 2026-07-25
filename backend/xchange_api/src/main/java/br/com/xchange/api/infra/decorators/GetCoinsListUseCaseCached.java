package br.com.xchange.api.infra.decorators;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import br.com.xchange.api.domain.entities.CoinWithMarketData;
import br.com.xchange.api.domain.ports.usecases.GetCoinsListUseCasePort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetCoinsListUseCaseCached implements GetCoinsListUseCasePort {
  private final GetCoinsListUseCasePort getCoinsListUseCasePort;

  @Override
  @Cacheable(value = "coinsList")
  public List<CoinWithMarketData> execute() {
    return new ArrayList<>(this.getCoinsListUseCasePort.execute());
  }
}
