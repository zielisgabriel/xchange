package br.com.xchange.api.domain.ports.repositories;

import java.util.Optional;
import java.util.UUID;

import br.com.xchange.api.domain.entities.RefreshToken;

public interface RefreshTokenRepositoryPort {
  RefreshToken save(RefreshToken refreshToken);
  Optional<RefreshToken> findById(UUID id);
  Optional<RefreshToken> findByUserId(UUID userId);
}
