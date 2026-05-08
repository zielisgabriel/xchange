package br.com.xchange.api.infra.services.coingecko.dto;

import java.math.BigInteger;

public record CoinsListWithMarketData(
  String id,
  String symbol,
  String name,
  String image,
  BigInteger current_price
) {}
