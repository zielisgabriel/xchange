package br.com.xchange.api.domain.entities;

import java.util.UUID;

import br.com.xchange.api.domain.valueobject.BirthDate;
import br.com.xchange.api.domain.valueobject.Cpf;
import lombok.Data;

@Data
public class AuthUser {
  private UUID id;
  private String email;
  private String password;
  private BirthDate birthDate;
  private Cpf cpf;
}
