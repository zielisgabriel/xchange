package br.com.xchange.api.application.usecase;

import org.springframework.stereotype.Service;

import br.com.xchange.api.domain.entities.CoinChartData;
import br.com.xchange.api.domain.ports.services.CoinServicePort;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetCoinChartDataUseCase {
  private final CoinServicePort coinServicePort;

  public CoinChartData execute(String coinId) {
    return this.coinServicePort.getChartDataById(coinId);
  }
}
