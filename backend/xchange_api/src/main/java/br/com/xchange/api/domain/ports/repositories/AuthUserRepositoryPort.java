package br.com.xchange.api.domain.ports.repositories;

import java.util.Optional;
import java.util.UUID;

import br.com.xchange.api.domain.entities.AuthUser;

public interface AuthUserRepositoryPort {
  Optional<AuthUser> findByEmail(String email);
  Optional<AuthUser> findById(UUID id);
  AuthUser save(AuthUser authUser);
}
