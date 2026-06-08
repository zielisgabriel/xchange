package br.com.xchange.api.infra.services.coingecko.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import br.com.xchange.api.domain.entities.FavoriteCoin;
import lombok.Data;

@Data
public class FavoriteCoinsRequestDTO {
  @NotNull(message = "A lista de moedas favoritas não pode ser nula.")
  @Size(max = 5, message = "No máximo 5 moedas favoritas por usuário.")
  @Valid
  private List<FavoriteCoin> favoriteCoins;
}
