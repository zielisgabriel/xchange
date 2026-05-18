package br.com.xchange.api.domain.exceptions;

public class RefreshTokenNotFoundException extends RuntimeException {
  public RefreshTokenNotFoundException() {
    super("Refresh token não encontrado ou expirado!");
  }
}
