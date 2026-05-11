package br.com.xchange.api.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidUserException extends RuntimeException {
  public InvalidUserException() {
    super("Usuário inválido, faça o login novamente!");
  }
}
