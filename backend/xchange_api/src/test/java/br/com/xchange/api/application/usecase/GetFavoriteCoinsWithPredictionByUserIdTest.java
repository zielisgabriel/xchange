package br.com.xchange.api.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.xchange.api.domain.entities.CoinPrediction;
import br.com.xchange.api.domain.entities.FavoriteCoin;
import br.com.xchange.api.domain.entities.FavoriteCoinWithPrediction;
import br.com.xchange.api.domain.entities.Profile;
import br.com.xchange.api.domain.exceptions.UserNotFoundException;
import br.com.xchange.api.domain.ports.repositories.ProfileRepositoryPort;
import br.com.xchange.api.domain.ports.services.RecommendationCoinServicePort;

@ExtendWith(MockitoExtension.class)
class GetFavoriteCoinsWithPredictionByUserIdTest {

  @Mock
  private ProfileRepositoryPort repositoryPort;

  @Mock
  private RecommendationCoinServicePort recommendationCoinServicePort;

  @InjectMocks
  private GetFavoriteCoinsWithPredictionByUserId useCase;

  private static final UUID USER_ID = UUID.randomUUID();

  private Profile profileWithCoin(String symbol) {
    FavoriteCoin coin = new FavoriteCoin();
    coin.setCoinId("bitcoin");
    coin.setName("Bitcoin");
    coin.setSymbol(symbol);
    coin.setImageUrl("https://img.com/btc.png");

    Profile profile = new Profile();
    profile.setFavoriteCoins(Set.of(coin));
    return profile;
  }

  private CoinPrediction prediction(String symbol, String value) {
    CoinPrediction prediction = new CoinPrediction();
    prediction.setSymbol(symbol);
    prediction.setPrediction(value);
    return prediction;
  }

  @Test
  @DisplayName("Deve casar a predição da moeda favorita pelo símbolo")
  void shouldMatchPredictionBySymbol() {
    when(repositoryPort.findById(USER_ID)).thenReturn(Optional.of(profileWithCoin("BTC")));
    when(recommendationCoinServicePort.getCoinPredictionBySymbols(anyList()))
        .thenReturn(Set.of(prediction("BTC", "UP")));

    List<FavoriteCoinWithPrediction> result = useCase.execute(USER_ID);

    assertEquals(1, result.size());
    assertEquals("UP", result.get(0).getPrediction());
  }

  @Test
  @DisplayName("Deve retornar predição nula quando não há correspondência")
  void shouldReturnNullPredictionWhenNoMatch() {
    when(repositoryPort.findById(USER_ID)).thenReturn(Optional.of(profileWithCoin("BTC")));
    when(recommendationCoinServicePort.getCoinPredictionBySymbols(anyList()))
        .thenReturn(Set.of());

    List<FavoriteCoinWithPrediction> result = useCase.execute(USER_ID);

    assertEquals(1, result.size());
    assertNull(result.get(0).getPrediction());
  }

  @Test
  @DisplayName("Deve lançar UserNotFoundException quando o perfil não existe")
  void shouldThrowWhenProfileNotFound() {
    when(repositoryPort.findById(USER_ID)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> useCase.execute(USER_ID));
  }
}
