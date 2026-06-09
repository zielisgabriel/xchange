package br.com.xchange.api.domain.entities;

import lombok.Data;

@Data
public class FavoriteCoin {
  private String coinId;
  private String name;
  private String symbol;
  private String imageUrl;
}
