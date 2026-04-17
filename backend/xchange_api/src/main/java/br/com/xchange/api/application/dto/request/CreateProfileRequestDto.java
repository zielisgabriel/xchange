package br.com.xchange.api.application.dto.request;

import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.xchange.api.domain.entities.Profile;

public record CreateProfileRequestDto(
  @JsonProperty(value = "user_id")
  UUID userId,

  @JsonProperty(value = "favorite_cryptos")
  Set<String> favoriteCryptos
) {
  public Profile toDomain() {
    Profile profile = new Profile();
    profile.setId(userId);
    profile.setFavoriteCryptos(favoriteCryptos);

    return profile;
  }
}
