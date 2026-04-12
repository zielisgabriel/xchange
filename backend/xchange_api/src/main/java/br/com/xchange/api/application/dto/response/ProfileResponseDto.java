package br.com.xchange.api.application.dto.response;

import java.util.Set;
import java.util.UUID;

import br.com.xchange.api.domain.entities.Profile;

public record ProfileResponseDto(
  UUID id,
  AuthUserResponseDto authUser,
  Set<String> favoriteCryptos
) {
  public static ProfileResponseDto fromDomain(Profile profile) {
    if (profile == null) return null;

    return new ProfileResponseDto(
      profile.getId(),
      AuthUserResponseDto.fromDomain(profile.getAuthUser()),
      profile.getFavoriteCryptos()
    );
  }
}
