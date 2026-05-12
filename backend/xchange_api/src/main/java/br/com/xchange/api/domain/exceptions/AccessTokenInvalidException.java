package br.com.xchange.api.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class AccessTokenInvalidException extends RuntimeException {
  public AccessTokenInvalidException() {
    super("Token de acesso inválido!");
  }

  public AccessTokenInvalidException(String message) {
    super(message);
  }
}
