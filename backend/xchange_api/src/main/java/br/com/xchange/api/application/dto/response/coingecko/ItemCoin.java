package br.com.xchange.api.application.dto.response.coingecko;

import java.math.BigDecimal;
import java.util.Optional;

record ItemCoin(
  String id,
  Integer coin_id,
  String name,
  String symbol,
  Integer market_cap_rank,
  String thumb,
  String small,
  String large,
  String slug,
  BigDecimal price_btc,
  Integer score,
  DataItemCoin data,
  String market_cap,
  String market_cap_btc,
  String total_volume,
  String total_volume_btc,
  String sparkline,
  Optional<Object> content
) {}
