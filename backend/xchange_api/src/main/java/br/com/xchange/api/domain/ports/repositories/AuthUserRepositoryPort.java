package br.com.xchange.api.domain.ports.repositories;

import java.util.Optional;

import br.com.xchange.api.domain.entities.AuthUser;

public interface AuthUserRepositoryPort {
  Optional<AuthUser> findByEmail(String email);
}
