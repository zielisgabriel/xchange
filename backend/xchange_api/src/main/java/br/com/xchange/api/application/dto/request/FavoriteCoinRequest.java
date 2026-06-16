package br.com.xchange.api.application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.xchange.api.domain.entities.FavoriteCoin;
import jakarta.validation.constraints.NotBlank;

public record FavoriteCoinRequest(
  @JsonProperty(value = "coinId")
  @NotBlank(message = "O id da moeda é obrigatório!")
  String coinId,

  @NotBlank(message = "O nome da moeda é obrigatório!")
  String name,

  @NotBlank(message = "O símbolo da moeda é obrigatório!")
  String symbol,

  @JsonProperty(value = "imageUrl")
  String imageUrl
) {
  public FavoriteCoin toDomain() {
    FavoriteCoin favoriteCoin = new FavoriteCoin();
    favoriteCoin.setCoinId(coinId);
    favoriteCoin.setName(name);
    favoriteCoin.setSymbol(symbol);
    favoriteCoin.setImageUrl(imageUrl);

    return favoriteCoin;
  }
}
