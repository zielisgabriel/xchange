package br.com.xchange.api.infra.controllers;

import java.security.Principal;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.xchange.api.application.dto.response.FavoriteCoinStatusResponse;
import br.com.xchange.api.application.usecase.AddFavoriteCoinUseCase;
import br.com.xchange.api.application.usecase.GetFavoriteCoinsStatusUseCase;
import br.com.xchange.api.application.usecase.RemoveFavoriteCoinUseCase;
import br.com.xchange.api.application.utils.PrincipalUtils;
import br.com.xchange.api.infra.services.coingecko.dto.AddFavoriteCoinRequestDTO;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteCoinsController {

  private final GetFavoriteCoinsStatusUseCase getFavoriteCoinsStatusUseCase;
  private final AddFavoriteCoinUseCase addFavoriteCoinUseCase;
  private final RemoveFavoriteCoinUseCase removeFavoriteCoinUseCase;

  @GetMapping("/status")
  public ResponseEntity<List<FavoriteCoinStatusResponse>> getFavoritesStatus(Principal principal) {
    List<FavoriteCoinStatusResponse> response = getFavoriteCoinsStatusUseCase.execute(PrincipalUtils.recoverUserId(principal));
    return ResponseEntity.ok(response);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void addFavorite(Principal principal, @Valid @RequestBody AddFavoriteCoinRequestDTO request) {
    addFavoriteCoinUseCase.execute(PrincipalUtils.recoverUserId(principal), request);
  }

  @DeleteMapping("/{coinId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void removeFavorite(Principal principal, @PathVariable String coinId) {
    removeFavoriteCoinUseCase.execute(PrincipalUtils.recoverUserId(principal), coinId);
  }
}
