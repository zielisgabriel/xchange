package br.com.xchange.api.domain.ports.repositories;

import java.util.Optional;
import java.util.UUID;

import br.com.xchange.api.domain.entities.RefreshToken;

public interface RefreshTokenRepositoryPort {
  public RefreshToken save(RefreshToken refreshToken);
  public Optional<RefreshToken> findById(UUID id);
  public Optional<RefreshToken> findByUserId(UUID userId);
}
