package br.com.xchange.api.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record LoginUserRequest(
  @Email(message = "E-mail invalido!")
  String email,
  @Size(message = "Senha inválida!", min = 8, max = 45)
  String password
) {}
