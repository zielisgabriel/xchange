package br.com.xchange.api.domain.exceptions;

public class AccessTokenInvalidException extends RuntimeException {
  public AccessTokenInvalidException() {
    super("Token de acesso inválido!");
  }

  public AccessTokenInvalidException(String message) {
    super(message);
  }
}
