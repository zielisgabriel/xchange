package br.com.xchange.api.infra.adapters.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import br.com.xchange.api.domain.entities.Profile;
import br.com.xchange.api.domain.exceptions.InvalidUserException;
import br.com.xchange.api.domain.ports.repositories.ProfileRepositoryPort;
import br.com.xchange.api.infra.adapters.implementations.JpaAuthUserRepositoryImpl;
import br.com.xchange.api.infra.adapters.implementations.JpaProfileRepositoryImpl;
import br.com.xchange.api.infra.entities.AuthUserJpa;
import br.com.xchange.api.infra.entities.ProfileJpa;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProfileRepositoryAdapter implements ProfileRepositoryPort {
  private final JpaProfileRepositoryImpl jpaProfileRepositoryImpl;
  private final JpaAuthUserRepositoryImpl jpaAuthUserRepositoryImpl;

  @Override
  public Optional<Profile> findById(UUID id) {
    return this.jpaProfileRepositoryImpl.findById(id)
      .map(ProfileJpa::toDomain);
  }

  @Override
  public Profile save(Profile profile) {
    AuthUserJpa authUser = this.jpaAuthUserRepositoryImpl.findById(profile.getId())
      .orElseThrow(() -> new InvalidUserException());

    ProfileJpa profileJpa = new ProfileJpa();
    profileJpa.setAuthUserJpa(authUser);
    profileJpa.setFavoriteCryptos(profile.getFavoriteCryptos());

    return this.jpaProfileRepositoryImpl.save(profileJpa).toDomain();
  }
}
