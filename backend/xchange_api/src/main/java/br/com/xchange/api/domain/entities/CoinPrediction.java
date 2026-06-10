package br.com.xchange.api.domain.entities;

import lombok.Data;

@Data
public class CoinPrediction {
  private String id;
  private String symbol;
  private String name;
  private String prediction;
}
