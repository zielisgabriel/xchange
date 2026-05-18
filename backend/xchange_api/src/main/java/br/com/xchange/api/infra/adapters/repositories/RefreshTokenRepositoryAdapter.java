package br.com.xchange.api.infra.adapters.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import br.com.xchange.api.domain.entities.RefreshToken;
import br.com.xchange.api.domain.ports.repositories.RefreshTokenRepositoryPort;
import br.com.xchange.api.infra.adapters.implementations.RedisRefreshTokenRepositoryImpl;
import br.com.xchange.api.infra.entities.RefreshTokenRedis;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryAdapter implements RefreshTokenRepositoryPort {
  private final RedisRefreshTokenRepositoryImpl repository;

  @Override
  public RefreshToken save(RefreshToken refreshToken) {
    RefreshTokenRedis refreshTokenRedis = this.repository
      .save(RefreshTokenRedis.fromDomain(refreshToken));

    return refreshTokenRedis.toDomain();
  }

  @Override
  public Optional<RefreshToken> findById(UUID id) {
    return this.repository.findById(id).map(RefreshTokenRedis::toDomain);
  }

  @Override
  public Optional<RefreshToken> findByUserId(UUID userId) {
    return this.repository.findByUserId(userId).map(RefreshTokenRedis::toDomain);
  }
}
