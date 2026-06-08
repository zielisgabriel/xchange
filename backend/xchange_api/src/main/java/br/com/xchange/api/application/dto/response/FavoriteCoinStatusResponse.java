package br.com.xchange.api.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteCoinStatusResponse {
  private String id;
  private String name;
  private String symbol;
  private String imageUrl;
  private String status;
}
