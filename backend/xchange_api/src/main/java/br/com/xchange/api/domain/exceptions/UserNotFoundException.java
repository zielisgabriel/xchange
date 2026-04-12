package br.com.xchange.api.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException() {
    super("Usuário não encontrado.");
  }
}
