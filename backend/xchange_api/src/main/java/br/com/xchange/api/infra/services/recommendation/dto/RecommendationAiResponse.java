package br.com.xchange.api.infra.services.recommendation.dto;

import java.util.List;

import lombok.Data;

@Data
public class RecommendationAiResponse {
  private String last_updated;
  private List<CoinPrediction> data;

  @Data
  public static class CoinPrediction {
    private String id;
    private String symbol;
    private String name;
    private Double current_price;
    private Double price_change_percentage_24h;
    private String prediction;
  }
}
