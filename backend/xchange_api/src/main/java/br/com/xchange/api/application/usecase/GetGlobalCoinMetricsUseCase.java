package br.com.xchange.api.application.usecase;

import org.springframework.stereotype.Service;

import br.com.xchange.api.application.dto.response.GlobalCoinMetricsResponse;
import br.com.xchange.api.domain.entities.GlobalCoinMetricsData;
import br.com.xchange.api.domain.ports.services.CoinServicePort;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetGlobalCoinMetricsUseCase {
  private final CoinServicePort service;

  public GlobalCoinMetricsResponse execute() {
    GlobalCoinMetricsData metrics = this.service.getGlobalCoinMetrics();

    return new GlobalCoinMetricsResponse(new GlobalCoinMetricsResponse.Data(
      metrics.getTotalMarketCap(),
      metrics.getTotalVolume(),
      metrics.getMarketCapChangePercentage24hUsd(),
      metrics.getVolumeChangePercentage24hUsd()
    ));
  }
}
