package br.com.xchange.api.infra.adapters.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.xchange.api.domain.entities.Profile;
import br.com.xchange.api.domain.exceptions.UserNotFoundException;
import br.com.xchange.api.domain.ports.repositories.ProfileRepositoryPort;
import br.com.xchange.api.infra.adapters.implementations.JpaAuthUserRepositoryImpl;
import br.com.xchange.api.infra.adapters.implementations.JpaProfileRepositoryImpl;
import br.com.xchange.api.infra.entities.AuthUserJpa;
import br.com.xchange.api.infra.entities.ProfileJpa;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProfileRepositoryAdapter implements ProfileRepositoryPort {
  private final JpaProfileRepositoryImpl jpaProfileRepositoryImpl;
  private final JpaAuthUserRepositoryImpl jpaAuthUserRepositoryImpl;
  private final EntityManager entityManager;

  @Override
  public Optional<Profile> findById(UUID id) {
    return this.jpaProfileRepositoryImpl.findById(id)
      .map(ProfileJpa::toDomain);
  }

  @Override
  @Transactional
  public Profile save(Profile profile) {
    AuthUserJpa authUser = this.jpaAuthUserRepositoryImpl.findById(profile.getId())
      .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado para criar perfil."));

    ProfileJpa profileJpa = new ProfileJpa();
    profileJpa.setAuthUserJpa(authUser);
    profileJpa.setFavoriteCryptos(profile.getFavoriteCryptos());

    entityManager.persist(profileJpa);

    return profileJpa.toDomain();
  }
}
