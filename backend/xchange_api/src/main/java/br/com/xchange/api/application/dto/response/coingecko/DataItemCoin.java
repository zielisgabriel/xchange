package br.com.xchange.api.application.dto.response.coingecko;

import java.math.BigDecimal;
import java.util.Map;

record DataItemCoin(
  BigDecimal price,
  BigDecimal price_btc,
  Map<String, BigDecimal> price_change_percentage_24h
) {}
