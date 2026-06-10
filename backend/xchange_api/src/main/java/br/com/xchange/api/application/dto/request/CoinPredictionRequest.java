package br.com.xchange.api.application.dto.request;

import java.util.List;

public record CoinPredictionRequest(
  List<String> symbols
) {}
