package br.com.xchange.api.application.dto.response;

import java.util.UUID;

import br.com.xchange.api.domain.entities.Profile;

public record ProfileSimpleResponseDto(
  UUID id,
  String firstName
) {
  public static ProfileSimpleResponseDto fromDomain(Profile profile) {
    return new ProfileSimpleResponseDto(profile.getId(), profile.getAuthUser().getFirstName());
  }
}
