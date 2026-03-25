package br.com.xchange.api.domain.exceptions;

import org.springframework.security.core.AuthenticationException;

public class EmailOrPasswordInvalidException extends AuthenticationException {
  public EmailOrPasswordInvalidException() {
    super("Email ou senha inválida!");
  }
}
