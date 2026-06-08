package br.com.xchange.api.application.usecase;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.xchange.api.domain.entities.FavoriteCoin;
import br.com.xchange.api.domain.entities.Profile;
import br.com.xchange.api.domain.ports.repositories.ProfileRepositoryPort;
import br.com.xchange.api.infra.services.coingecko.dto.AddFavoriteCoinRequestDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddFavoriteCoinUseCase {

  private final ProfileRepositoryPort profileRepositoryPort;

  public void execute(UUID userId, AddFavoriteCoinRequestDTO request) {
    Profile profile = profileRepositoryPort.findById(userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil não encontrado"));

    if (profile.getFavoriteCoins().size() >= 5) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No máximo 5 moedas favoritas por usuário.");
    }

    // Check if already exists
    boolean exists = profile.getFavoriteCoins().stream()
        .anyMatch(c -> c.getCoinId().equals(request.getCoinId()));
    
    if (exists) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Moeda já está nos favoritos.");
    }

    FavoriteCoin favoriteCoin = new FavoriteCoin();
    favoriteCoin.setCoinId(request.getCoinId());
    favoriteCoin.setName(request.getName());
    favoriteCoin.setSymbol(request.getSymbol());

    profile.getFavoriteCoins().add(favoriteCoin);
    profileRepositoryPort.save(profile);
  }
}
