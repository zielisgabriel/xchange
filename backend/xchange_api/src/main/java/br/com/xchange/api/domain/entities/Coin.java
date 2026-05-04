package br.com.xchange.api.domain.entities;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Coin {
  private String id;
  private String name;
  private String symbol;
  private String imageUrl;
  private BigDecimal price;
  private String updatedAt;
}
