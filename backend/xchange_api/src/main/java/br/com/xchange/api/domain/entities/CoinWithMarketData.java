package br.com.xchange.api.domain.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class CoinWithMarketData extends Coin {
  private Double priceBtc;
  private String marketCap;
  private String totalVolume;
  private String sparkline;
}
