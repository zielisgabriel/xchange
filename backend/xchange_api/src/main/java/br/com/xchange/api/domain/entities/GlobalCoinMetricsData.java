package br.com.xchange.api.domain.entities;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class GlobalCoinMetricsData {
  private BigDecimal totalMarketCap;
  private BigDecimal totalVolume;
  private BigDecimal marketCapChangePercentage24hUsd;
  private BigDecimal volumeChangePercentage24hUsd;
}
