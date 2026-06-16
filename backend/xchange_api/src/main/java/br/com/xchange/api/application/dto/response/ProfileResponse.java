package br.com.xchange.api.application.dto.response;

import java.util.Set;
import java.util.UUID;

import br.com.xchange.api.domain.entities.FavoriteCoin;
import br.com.xchange.api.domain.entities.Profile;

public record ProfileResponse(
  UUID id,
  AuthUserResponse authUser,
  Set<FavoriteCoin> favoriteCoins
) {
  public static ProfileResponse fromDomain(Profile profile) {
    if (profile == null) return null;

    return new ProfileResponse(
      profile.getId(),
      AuthUserResponse.fromDomain(profile.getAuthUser()),
      profile.getFavoriteCoins()
    );
  }
}
