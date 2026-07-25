package br.com.xchange.api.infra.decorators;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import br.com.xchange.api.domain.entities.Coin;
import br.com.xchange.api.domain.ports.usecases.GetSimpleCoinUseCasePort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetSimpleCoinUseCaseCached implements GetSimpleCoinUseCasePort {
  private final GetSimpleCoinUseCasePort getSimpleCoinUseCasePort;

  @Override
  @Cacheable(value = "simpleCoinsList")
  public List<Coin> execute() {
    return this.getSimpleCoinUseCasePort.execute();
  }
}
