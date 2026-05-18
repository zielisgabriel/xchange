package br.com.xchange.api.domain.exceptions;

public class RefreshTokenAlreadyExistsException extends RuntimeException {
  public RefreshTokenAlreadyExistsException() {
    super("Refresh token já existe!");
  }
}
