package br.com.xchange.api.domain.exceptions;

public class ForbiddenChangeAnotherUserInfoException extends RuntimeException {
  public ForbiddenChangeAnotherUserInfoException() {
    super("É proibido alterar as informações de outro usuário!");
  }

  public ForbiddenChangeAnotherUserInfoException(String message) {
    super(message);
  }
}
