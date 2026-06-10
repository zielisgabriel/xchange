package br.com.xchange.api.infra.services.crypto_recommendation_ai.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CoinPredictionResponse(
  int total,
  List<CoinPredictionData> data
) {

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record CoinPredictionData(
    String id,
    String symbol,
    String name,
    String prediction
  ) {}
}
