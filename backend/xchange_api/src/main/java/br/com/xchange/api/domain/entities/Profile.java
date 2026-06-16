package br.com.xchange.api.domain.entities;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import br.com.xchange.api.domain.exceptions.FavoriteCoinsLimitExceededException;
import lombok.Data;

@Data
public class Profile {
  public static final int MAX_FAVORITE_COINS = 5;

  private UUID id;
  private AuthUser authUser;
  private Set<FavoriteCoin> favoriteCoins = new HashSet<FavoriteCoin>();

  public void addFavoriteCoin(FavoriteCoin coin) {
    boolean alreadyFavorite = this.favoriteCoins.stream()
      .anyMatch(favorite -> favorite.getCoinId().equals(coin.getCoinId()));

    if (alreadyFavorite) {
      return;
    }

    if (this.favoriteCoins.size() >= MAX_FAVORITE_COINS) {
      throw new FavoriteCoinsLimitExceededException();
    }

    this.favoriteCoins.add(coin);
  }

  public void removeFavoriteCoin(String coinId) {
    this.favoriteCoins.removeIf(favorite -> favorite.getCoinId().equals(coinId));
  }
}
