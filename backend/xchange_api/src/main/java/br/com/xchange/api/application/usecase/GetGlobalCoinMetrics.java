package br.com.xchange.api.application.usecase;

import org.springframework.stereotype.Service;

import br.com.xchange.api.application.dto.response.GlobalCoinMetricsResponse;
import br.com.xchange.api.domain.ports.services.CoinServicePort;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetGlobalCoinMetrics {
  private final CoinServicePort service;

  public GlobalCoinMetricsResponse execute() {
    return this.service.getGlobalCoinMetrics();
  }
}
