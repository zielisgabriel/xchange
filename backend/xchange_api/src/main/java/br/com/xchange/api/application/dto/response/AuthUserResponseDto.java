package br.com.xchange.api.application.dto.response;

import java.time.LocalDate;

import br.com.xchange.api.domain.entities.AuthUser;

public record AuthUserResponseDto(
  String firstName,
  String lastName,
  String email,
  LocalDate birthDate,
  String cpf
) {
  public static AuthUserResponseDto fromDomain(AuthUser authUser) {
    if (authUser == null) return null;
    
    return new AuthUserResponseDto(
      authUser.getFirstName(),
      authUser.getLastName(),
      authUser.getEmail(),
      authUser.getBirthDate() != null ? authUser.getBirthDate().getValue() : null,
      authUser.getCpf() != null ? authUser.getCpf().getValue() : null
    );
  }
}
