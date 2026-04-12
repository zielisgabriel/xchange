package br.com.xchange.api.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class ForbiddenChangeAnotherUserInfoException extends RuntimeException {
  public ForbiddenChangeAnotherUserInfoException() {
    super("É proibido alterar as informações de outro usuário!");
  }

  public ForbiddenChangeAnotherUserInfoException(String message) {
    super(message);
  }
}
