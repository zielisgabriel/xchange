package br.com.xchange.api.application.usecase;

import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.xchange.api.domain.entities.FavoriteCoin;
import br.com.xchange.api.domain.entities.Profile;
import br.com.xchange.api.domain.exceptions.UserNotFoundException;
import br.com.xchange.api.domain.ports.repositories.ProfileRepositoryPort;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetFavoriteCoinsByUserId {
  private final ProfileRepositoryPort repositoryPort;

  public Set<FavoriteCoin> execute(UUID userId) {
    Profile profile = this.repositoryPort.findById(userId)
      .orElseThrow(() -> new UserNotFoundException());

    return profile.getFavoriteCoins();
  }
}
