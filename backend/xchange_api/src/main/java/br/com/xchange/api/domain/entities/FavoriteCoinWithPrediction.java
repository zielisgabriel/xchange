package br.com.xchange.api.domain.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class FavoriteCoinWithPrediction extends FavoriteCoin {
  private String prediction;
}
