package br.com.xchange.api.application.dto.response;

import java.math.BigDecimal;

public record GlobalCoinMetricsResponse(
  Data data
) {
  public record Data(
    BigDecimal totalMarketCap,
    BigDecimal totalVolume,
    BigDecimal marketCapChangePercentage24hUsd,
    BigDecimal volumeChangePercentage24hUsd
  ) {}
}
