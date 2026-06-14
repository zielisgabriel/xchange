package br.com.xchange.api.domain.exceptions;

public class InvalidUserException extends RuntimeException {
  public InvalidUserException() {
    super("Usuário inválido, faça o login novamente!");
  }
}
