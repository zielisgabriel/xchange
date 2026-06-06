package br.com.xchange.api.infra.entities;

import br.com.xchange.api.domain.entities.FavoriteCoin;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class FavoriteCoinsJpa {
  @Column(name = "coin_id")
  private String coinId;
  private String name;
  private String symbol;

  public FavoriteCoin toDomain() {
    FavoriteCoin favoriteCoinsDomain = new FavoriteCoin();

    favoriteCoinsDomain.setCoinId(coinId);
    favoriteCoinsDomain.setName(name);
    favoriteCoinsDomain.setSymbol(symbol);

    return favoriteCoinsDomain;
  }

  public static FavoriteCoinsJpa fromDomain(FavoriteCoin favoriteCoinsDomain) {
    FavoriteCoinsJpa favoriteCoinsJpa = new FavoriteCoinsJpa();

    favoriteCoinsJpa.setCoinId(favoriteCoinsDomain.getCoinId());
    favoriteCoinsJpa.setName(favoriteCoinsDomain.getName());
    favoriteCoinsJpa.setSymbol(favoriteCoinsDomain.getSymbol());

    return favoriteCoinsJpa;
  }
}
