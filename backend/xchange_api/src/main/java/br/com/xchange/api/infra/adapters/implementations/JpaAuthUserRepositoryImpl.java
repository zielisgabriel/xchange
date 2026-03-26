package br.com.xchange.api.infra.adapters.implementations;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.xchange.api.infra.entities.AuthUserJpa;

public interface JpaAuthUserRepositoryImpl extends JpaRepository<AuthUserJpa, UUID> {
  Optional<AuthUserJpa> findByEmail(String email);
}
