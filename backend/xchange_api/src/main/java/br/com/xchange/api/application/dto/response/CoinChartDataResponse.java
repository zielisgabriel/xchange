package br.com.xchange.api.application.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record CoinChartDataResponse(
  List<List<BigDecimal>> prices
) {}
