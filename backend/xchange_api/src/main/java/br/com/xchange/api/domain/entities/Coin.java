package br.com.xchange.api.domain.entities;

import lombok.Data;

@Data
public class Coin {
  private String id;
  private String name;
  private String symbol;
  private String imageUrl;
  private Integer price;
  private String updatedAt;
}
