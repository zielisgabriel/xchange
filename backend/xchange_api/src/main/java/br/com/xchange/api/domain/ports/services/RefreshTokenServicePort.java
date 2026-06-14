package br.com.xchange.api.domain.ports.services;

import java.util.UUID;

import br.com.xchange.api.domain.entities.RefreshToken;

public interface RefreshTokenServicePort {
  String generate(Object principal);
  RefreshToken validate(UUID refreshTokenId);
}
