package br.com.xchange.api.infra.adapters.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import br.com.xchange.api.domain.entities.AuthUser;
import br.com.xchange.api.domain.ports.repositories.AuthUserRepositoryPort;
import br.com.xchange.api.infra.adapters.implementations.JpaAuthUserRepositoryImpl;
import br.com.xchange.api.infra.entities.AuthUserJpa;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AuthUserRepositoryAdapter implements AuthUserRepositoryPort {
  private final JpaAuthUserRepositoryImpl jpaAuthUserRepositoryImpl;

  @Override
  public Optional<AuthUser> findByEmail(String email) {
    return this.jpaAuthUserRepositoryImpl.findByEmail(email)
      .map(AuthUserJpa::toDomain);
  }

  @Override
  public Optional<AuthUser> findById(UUID id) {
    return this.jpaAuthUserRepositoryImpl.findById(id)
      .map(AuthUserJpa::toDomain);
  }

  @Override
  public AuthUser save(AuthUser authUser) {
    return this.jpaAuthUserRepositoryImpl.save(AuthUserJpa.fromDomain(authUser))
      .toDomain();
  }
}
