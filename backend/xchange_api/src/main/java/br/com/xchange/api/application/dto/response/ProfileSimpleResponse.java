package br.com.xchange.api.application.dto.response;

import java.util.Set;
import java.util.UUID;

import br.com.xchange.api.domain.entities.FavoriteCoin;
import br.com.xchange.api.domain.entities.Profile;

public record ProfileSimpleResponse(
  UUID id,
  String firstName,
  boolean onboardingFinished,
  Set<FavoriteCoin> favoriteCoins
) {
  public static ProfileSimpleResponse fromDomain(Profile profile) {
    return new ProfileSimpleResponse(
      profile.getId(),
      profile.getAuthUser().getFirstName(),
      profile.getAuthUser().isOnboardingFinished(),
      profile.getFavoriteCoins()
    );
  }
}
