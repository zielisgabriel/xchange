package br.com.xchange.api.application.dto.response;

import java.util.UUID;

import br.com.xchange.api.domain.entities.AuthUser;

public record ProfileSimpleResponseDto(
  UUID id,
  String firstName
) {
  public static ProfileSimpleResponseDto fromDomain(AuthUser authUser) {
    return new ProfileSimpleResponseDto(authUser.getId(), authUser.getFirstName());
  }
}
