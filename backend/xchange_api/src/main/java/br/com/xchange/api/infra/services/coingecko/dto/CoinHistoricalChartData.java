package br.com.xchange.api.infra.services.coingecko.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CoinHistoricalChartData(
    List<List<BigDecimal>> prices,
    @JsonProperty("market_caps") List<List<BigDecimal>> marketCaps,
    @JsonProperty("total_volumes") List<List<BigDecimal>> totalVolumes
) {}
