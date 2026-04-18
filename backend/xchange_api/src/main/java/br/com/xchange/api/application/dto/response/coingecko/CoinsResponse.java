package br.com.xchange.api.application.dto.response.coingecko;

import java.util.List;

public record CoinsResponse(
  List<ItemCoin> coins
) {}
