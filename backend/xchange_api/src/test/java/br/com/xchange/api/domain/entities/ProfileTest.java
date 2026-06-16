package br.com.xchange.api.domain.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.xchange.api.domain.exceptions.FavoriteCoinsLimitExceededException;

class ProfileTest {

  private FavoriteCoin coin(String coinId) {
    FavoriteCoin favoriteCoin = new FavoriteCoin();
    favoriteCoin.setCoinId(coinId);
    favoriteCoin.setName(coinId);
    favoriteCoin.setSymbol(coinId.toUpperCase());
    return favoriteCoin;
  }

  private Profile profileWith(int favorites) {
    Profile profile = new Profile();
    for (int i = 0; i < favorites; i++) {
      profile.getFavoriteCoins().add(coin("coin-" + i));
    }
    return profile;
  }

  @Test
  @DisplayName("Deve adicionar uma moeda favorita")
  void shouldAddFavoriteCoin() {
    Profile profile = new Profile();

    profile.addFavoriteCoin(coin("bitcoin"));

    assertEquals(1, profile.getFavoriteCoins().size());
  }

  @Test
  @DisplayName("Não deve duplicar uma favorita já existente (idempotente por coinId)")
  void shouldNotDuplicateExistingFavorite() {
    Profile profile = new Profile();
    profile.addFavoriteCoin(coin("bitcoin"));

    profile.addFavoriteCoin(coin("bitcoin"));

    assertEquals(1, profile.getFavoriteCoins().size());
  }

  @Test
  @DisplayName("Deve lançar exceção ao exceder o limite de 5 favoritas")
  void shouldThrowWhenExceedingLimit() {
    Profile profile = profileWith(Profile.MAX_FAVORITE_COINS);

    assertThrows(FavoriteCoinsLimitExceededException.class,
        () -> profile.addFavoriteCoin(coin("new-coin")));
  }

  @Test
  @DisplayName("Deve remover uma moeda favorita pelo coinId")
  void shouldRemoveFavoriteByCoinId() {
    Profile profile = new Profile();
    profile.addFavoriteCoin(coin("bitcoin"));
    profile.addFavoriteCoin(coin("ethereum"));

    profile.removeFavoriteCoin("bitcoin");

    assertEquals(1, profile.getFavoriteCoins().size());
    assertTrue(profile.getFavoriteCoins().stream().anyMatch(c -> c.getCoinId().equals("ethereum")));
    assertFalse(profile.getFavoriteCoins().stream().anyMatch(c -> c.getCoinId().equals("bitcoin")));
  }

  @Test
  @DisplayName("Remover uma moeda inexistente não deve falhar")
  void shouldIgnoreRemovingUnknownCoin() {
    Profile profile = new Profile();
    profile.addFavoriteCoin(coin("bitcoin"));

    profile.removeFavoriteCoin("dogecoin");

    assertEquals(1, profile.getFavoriteCoins().size());
  }
}
