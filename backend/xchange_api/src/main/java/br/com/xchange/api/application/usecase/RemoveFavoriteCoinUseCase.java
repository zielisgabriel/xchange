package br.com.xchange.api.application.usecase;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.xchange.api.domain.entities.Profile;
import br.com.xchange.api.domain.ports.repositories.ProfileRepositoryPort;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RemoveFavoriteCoinUseCase {

  private final ProfileRepositoryPort profileRepositoryPort;

  public void execute(UUID userId, String coinId) {
    Profile profile = profileRepositoryPort.findById(userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil não encontrado"));

    boolean removed = profile.getFavoriteCoins().removeIf(c -> c.getCoinId().equals(coinId));
    
    if (!removed) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Moeda não encontrada nos favoritos.");
    }

    profileRepositoryPort.save(profile);
  }
}
