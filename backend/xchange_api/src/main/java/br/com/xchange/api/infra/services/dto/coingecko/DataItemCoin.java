package br.com.xchange.api.infra.services.dto.coingecko;

import java.math.BigDecimal;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DataItemCoin(
  BigDecimal price,
  BigDecimal price_btc,
  Map<String, BigDecimal> price_change_percentage_24h
) {}
