package br.com.xchange.api.application.dto.response;

import java.time.LocalDate;

import br.com.xchange.api.domain.entities.AuthUser;

public record AuthUserResponse(
  String firstName,
  String lastName,
  String email,
  LocalDate birthDate,
  String cpf
) {
  public static AuthUserResponse fromDomain(AuthUser authUser) {
    if (authUser == null) return null;
    
    return new AuthUserResponse(
      authUser.getFirstName(),
      authUser.getLastName(),
      authUser.getEmail(),
      authUser.getBirthDate() != null ? authUser.getBirthDate().getValue() : null,
      authUser.getCpf() != null ? authUser.getCpf().getValue() : null
    );
  }
}
