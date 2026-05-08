package br.com.xchange.api.domain.entities;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class CoinWithMarketData extends Coin {
  private BigDecimal priceBtc;
  private String marketCap;
  private String totalVolume;
  private String sparkline;
  private BigDecimal priceChangePercentage24h;
}
