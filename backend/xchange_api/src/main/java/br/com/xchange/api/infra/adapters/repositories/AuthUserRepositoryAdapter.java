package br.com.xchange.api.infra.adapters.repositories;

import java.util.Optional;

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
}
