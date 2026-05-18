package br.com.xchange.api.infra.adapters.implementations;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import br.com.xchange.api.infra.entities.RefreshTokenRedis;

public interface RedisRefreshTokenRepositoryImpl extends CrudRepository<RefreshTokenRedis, UUID> {
  Optional<RefreshTokenRedis> findByUserId(UUID userId);
}
