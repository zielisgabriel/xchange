package br.com.xchange.api.infra.services.coingecko.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddFavoriteCoinRequestDTO {
  @NotBlank(message = "O ID da moeda não pode ser vazio")
  private String coinId;
  
  @NotBlank(message = "O nome da moeda não pode ser vazio")
  private String name;
  
  @NotBlank(message = "O símbolo da moeda não pode ser vazio")
  private String symbol;
}
