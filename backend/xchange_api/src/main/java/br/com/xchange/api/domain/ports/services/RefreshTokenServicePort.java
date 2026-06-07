package br.com.xchange.api.domain.ports.services;

public interface RefreshTokenServicePort {
  public String generate(Object principal);
}
