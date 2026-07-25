package br.com.xchange.api.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.xchange.api.application.usecase.GetCoinsListUseCase;
import br.com.xchange.api.application.usecase.GetSimpleCoinUseCase;
import br.com.xchange.api.domain.ports.services.CoinServicePort;
import br.com.xchange.api.domain.ports.usecases.GetCoinsListUseCasePort;
import br.com.xchange.api.domain.ports.usecases.GetSimpleCoinUseCasePort;
import br.com.xchange.api.infra.decorators.GetCoinsListUseCaseCached;
import br.com.xchange.api.infra.decorators.GetSimpleCoinUseCaseCached;

@Configuration
public class CoinUseCaseConfig {
  @Bean
  public GetSimpleCoinUseCasePort getSimpleCoinUseCasePort(CoinServicePort coinServicePort) {
    GetSimpleCoinUseCase getSimpleCoinUseCase = new GetSimpleCoinUseCase(coinServicePort);

    return new GetSimpleCoinUseCaseCached(getSimpleCoinUseCase);
  }

  @Bean
  public GetCoinsListUseCasePort getCoinsListUseCasePort(CoinServicePort coinServicePort) {
    GetCoinsListUseCase getCoinsListUseCase = new GetCoinsListUseCase(coinServicePort);

    return new GetCoinsListUseCaseCached(getCoinsListUseCase);
  }
}
