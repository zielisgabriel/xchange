package br.com.xchange.api.infra.adapters.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import br.com.xchange.api.domain.entities.Profile;
import br.com.xchange.api.domain.ports.repositories.ProfileRepositoryPort;
import br.com.xchange.api.infra.adapters.implementations.JpaProfileRepositoryImpl;
import br.com.xchange.api.infra.entities.ProfileJpa;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProfileRepositoryAdapter implements ProfileRepositoryPort {
  private final JpaProfileRepositoryImpl jpaProfileRepositoryImpl;

  @Override
  public Optional<Profile> findById(UUID id) {
    return this.jpaProfileRepositoryImpl.findById(id)
      .map(ProfileJpa::toDomain);
  }

  @Transactional
  @Override
  public Profile save(Profile profile) {
    ProfileJpa profileJpa = ProfileJpa.fromDomain(profile);

    return this.jpaProfileRepositoryImpl.save(profileJpa).toDomain();
  }
}
