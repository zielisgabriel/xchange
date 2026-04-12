package br.com.xchange.api.domain.ports.repositories;

import java.util.Optional;
import java.util.UUID;

import br.com.xchange.api.domain.entities.Profile;

public interface ProfileRepositoryPort {
  public Optional<Profile> findById(UUID id);
  public Profile save(Profile profile);
}
