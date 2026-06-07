package br.com.xchange.api.application.dto.request;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.xchange.api.domain.entities.FavoriteCoin;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record OnboardingRequestDto(
  @JsonProperty(value = "favorite_coins")
  @NotNull(message = "A lista de moedas favoritas é obrigatória")
  @Size(min = 1, max = 5, message = "Selecione entre 1 e 5 moedas favoritas")
  Set<@Valid FavoriteCoin> favoriteCoins
) {}

