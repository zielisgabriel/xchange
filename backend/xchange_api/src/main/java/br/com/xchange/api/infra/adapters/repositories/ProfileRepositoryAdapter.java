package br.com.xchange.api.infra.adapters.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.xchange.api.domain.entities.Profile;
import br.com.xchange.api.domain.ports.repositories.ProfileRepositoryPort;
import br.com.xchange.api.infra.adapters.implementations.JpaAuthUserRepositoryImpl;
import br.com.xchange.api.infra.adapters.implementations.JpaProfileRepositoryImpl;
import br.com.xchange.api.infra.entities.AuthUserJpa;
import br.com.xchange.api.infra.entities.FavoriteCoinsJpa;
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
  @Transactional
  public Profile save(Profile profile) {
    ProfileJpa profileJpa;

    if (profile.getId() != null) {
      profileJpa = this.jpaProfileRepositoryImpl.findById(profile.getId())
        .orElseGet(() -> ProfileJpa.fromDomain(profile));
    } else {
      profileJpa = new ProfileJpa();
      if (profile.getAuthUser() != null) {
        AuthUserJpa authUser = this.jpaAuthUserRepositoryImpl.findById(profile.getAuthUser().getId())
          .orElseThrow(() -> new RuntimeException("User not found"));
        profileJpa.setAuthUserJpa(authUser);
      }
    }

    profileJpa.getFavoriteCoins().clear();

    profile.getFavoriteCoins().stream()
      .map(FavoriteCoinsJpa::fromDomain)
      .forEach(profileJpa.getFavoriteCoins()::add);

    if (profile.getAuthUser() != null && profileJpa.getAuthUserJpa() != null) {
      profileJpa.getAuthUserJpa().setOnboardingFinished(profile.getAuthUser().isOnboardingFinished());
    }

    return this.jpaProfileRepositoryImpl.save(profileJpa).toDomain();
  }
}
