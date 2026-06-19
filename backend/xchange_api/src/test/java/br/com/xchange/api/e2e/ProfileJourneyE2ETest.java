package br.com.xchange.api.e2e;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jayway.jsonpath.JsonPath;

import br.com.xchange.api.domain.entities.CoinPrediction;

@DisplayName("E2E - Jornada do perfil e moedas favoritas")
class ProfileJourneyE2ETest extends AbstractE2ETest {

  private String favoriteCoinJson(String coinId, String symbol) {
    return """
      { "coinId": "%s", "name": "%s", "symbol": "%s", "imageUrl": "https://img/%s.png" }
      """.formatted(coinId, coinId, symbol, coinId);
  }

  private String onboardingPayload(int amount) {
    String coins = IntStream.range(0, amount)
      .mapToObj(i -> favoriteCoinJson("coin" + i, "c" + i))
      .collect(Collectors.joining(","));

    return "{ \"favorite_coins\": [" + coins + "] }";
  }

  private int favoriteCoinsCount(String accessToken) {
    HttpResult response = get("/profile/favorite-coins", accessToken);
    assertEquals(200, response.status());
    return JsonPath.read(response.body(), "$.length()");
  }

  @Test
  @DisplayName("Deve fazer onboarding e gerenciar moedas favoritas ao longo da jornada")
  void shouldManageFavoritesThroughFullJourney() {
    Tokens tokens = registerAndLogin("favoritos@xchange.com");
    String token = tokens.accessToken();

    // Onboarding com 3 moedas
    HttpResult onboarding = post("/profile/onboarding", onboardingPayload(3), token);
    assertEquals(201, onboarding.status());
    assertEquals(3, favoriteCoinsCount(token));

    // Adiciona uma 4ª moeda
    HttpResult add = post("/profile/favorite-coins", favoriteCoinJson("ethereum", "eth"), token);
    assertEquals(201, add.status());
    assertEquals(4, favoriteCoinsCount(token));

    // Remove a moeda recém adicionada
    HttpResult remove = delete("/profile/favorite-coins/ethereum", token);
    assertEquals(200, remove.status());
    List<String> remainingIds = JsonPath.read(remove.body(), "$[*].coinId");
    assertFalse(remainingIds.contains("ethereum"));
    assertEquals(3, favoriteCoinsCount(token));
  }

  @Test
  @DisplayName("Deve impedir ultrapassar o limite de 5 moedas favoritas (409)")
  void shouldEnforceFavoriteLimit() {
    Tokens tokens = registerAndLogin("limite@xchange.com");
    String token = tokens.accessToken();

    assertEquals(201, post("/profile/onboarding", onboardingPayload(5), token).status());

    HttpResult sixth = post("/profile/favorite-coins", favoriteCoinJson("dogecoin", "doge"), token);
    assertEquals(409, sixth.status());
  }

  @Test
  @DisplayName("Deve retornar moedas favoritas com predição da IA")
  void shouldReturnFavoritesWithPrediction() {
    Tokens tokens = registerAndLogin("predicao@xchange.com");
    String token = tokens.accessToken();

    assertEquals(201,
      post("/profile/favorite-coins", favoriteCoinJson("bitcoin", "btc"), token).status());

    CoinPrediction prediction = new CoinPrediction();
    prediction.setSymbol("btc");
    prediction.setPrediction("UP");
    when(recommendationCoinServicePort.getCoinPredictionBySymbols(anyList()))
      .thenReturn(Set.of(prediction));

    HttpResult response = get("/profile/favorite-coins?prediction=true", token);
    assertEquals(200, response.status());
    assertEquals("bitcoin", JsonPath.read(response.body(), "$[0].coinId"));
    assertEquals("UP", JsonPath.read(response.body(), "$[0].prediction"));
  }

  @Test
  @DisplayName("Deve rejeitar onboarding sem moedas (422)")
  void shouldRejectEmptyOnboarding() {
    Tokens tokens = registerAndLogin("onboardingvazio@xchange.com");

    HttpResult response = post("/profile/onboarding",
      "{ \"favorite_coins\": [] }", tokens.accessToken());

    assertEquals(422, response.status());
  }
}
