package br.com.xchange.api.application.dto.response;

import java.util.Set;
import java.util.UUID;

import br.com.xchange.api.domain.entities.FavoriteCoin;
import br.com.xchange.api.domain.entities.Profile;

public record ProfileSimpleResponseDto(
  UUID id,
  String firstName,
  boolean onboardingFinished,
  Set<FavoriteCoin> favoriteCoins
) {
  public static ProfileSimpleResponseDto fromDomain(Profile profile) {
    return new ProfileSimpleResponseDto(
      profile.getId(),
      profile.getAuthUser().getFirstName(),
      profile.getAuthUser().isOnboardingFinished(),
      profile.getFavoriteCoins()
    );
  }
}
