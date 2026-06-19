package br.com.xchange.api.integration;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import br.com.xchange.api.domain.entities.CoinPrediction;
import br.com.xchange.api.domain.entities.Profile;

@DisplayName("Integração - ProfileController")
class ProfileControllerIntegrationTest extends AbstractIntegrationTest {

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

  @Test
  @DisplayName("Deve retornar 401 ao acessar o perfil sem autenticação")
  void shouldRejectUnauthenticated() throws Exception {
    mockMvc.perform(get("/profile/details"))
      .andExpect(status().isUnauthorized());
  }

  @Test
  @DisplayName("GET /profile/details deve retornar os dados do usuário autenticado")
  void shouldReturnProfileDetails() throws Exception {
    Profile profile = seedUser("details@xchange.com");

    mockMvc.perform(get("/profile/details")
        .header(HttpHeaders.AUTHORIZATION, bearerTokenFor(profile.getId())))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(profile.getId().toString()))
      .andExpect(jsonPath("$.authUser.email").value("details@xchange.com"))
      .andExpect(jsonPath("$.authUser.cpf").value(VALID_CPF));
  }

  @Test
  @DisplayName("GET /profile/simple deve retornar o resumo do perfil")
  void shouldReturnProfileSimple() throws Exception {
    Profile profile = seedUser("simple@xchange.com");

    mockMvc.perform(get("/profile/simple")
        .header(HttpHeaders.AUTHORIZATION, bearerTokenFor(profile.getId())))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(profile.getId().toString()))
      .andExpect(jsonPath("$.firstName").value("Jose"))
      .andExpect(jsonPath("$.onboardingFinished").value(false));
  }

  @Test
  @DisplayName("POST /profile/onboarding deve definir as moedas favoritas e retornar 201")
  void shouldFinishOnboarding() throws Exception {
    Profile profile = seedUser("onboarding@xchange.com");
    String token = bearerTokenFor(profile.getId());

    mockMvc.perform(post("/profile/onboarding")
        .header(HttpHeaders.AUTHORIZATION, token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(onboardingPayload(3)))
      .andExpect(status().isCreated());

    mockMvc.perform(get("/profile/favorite-coins")
        .header(HttpHeaders.AUTHORIZATION, token))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.length()").value(3));
  }

  @Test
  @DisplayName("POST /profile/onboarding deve retornar 422 com lista de moedas vazia")
  void shouldRejectEmptyOnboarding() throws Exception {
    Profile profile = seedUser("emptyonboarding@xchange.com");

    mockMvc.perform(post("/profile/onboarding")
        .header(HttpHeaders.AUTHORIZATION, bearerTokenFor(profile.getId()))
        .contentType(MediaType.APPLICATION_JSON)
        .content("{ \"favorite_coins\": [] }"))
      .andExpect(status().isUnprocessableEntity())
      .andExpect(jsonPath("$.message").value("Erro de validação!"));
  }

  @Test
  @DisplayName("POST /profile/favorite-coins deve adicionar uma moeda favorita e retornar 201")
  void shouldAddFavoriteCoin() throws Exception {
    Profile profile = seedUser("addfav@xchange.com");

    mockMvc.perform(post("/profile/favorite-coins")
        .header(HttpHeaders.AUTHORIZATION, bearerTokenFor(profile.getId()))
        .contentType(MediaType.APPLICATION_JSON)
        .content(favoriteCoinJson("ethereum", "eth")))
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$[?(@.coinId == 'ethereum')]").exists());
  }

  @Test
  @DisplayName("POST /profile/favorite-coins deve retornar 409 ao exceder o limite de 5 moedas")
  void shouldRejectWhenLimitExceeded() throws Exception {
    Profile profile = seedUser("limit@xchange.com");
    String token = bearerTokenFor(profile.getId());

    mockMvc.perform(post("/profile/onboarding")
        .header(HttpHeaders.AUTHORIZATION, token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(onboardingPayload(5)))
      .andExpect(status().isCreated());

    mockMvc.perform(post("/profile/favorite-coins")
        .header(HttpHeaders.AUTHORIZATION, token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(favoriteCoinJson("dogecoin", "doge")))
      .andExpect(status().isConflict());
  }

  @Test
  @DisplayName("DELETE /profile/favorite-coins/{coinId} deve remover a moeda favorita")
  void shouldRemoveFavoriteCoin() throws Exception {
    Profile profile = seedUser("removefav@xchange.com");
    String token = bearerTokenFor(profile.getId());

    mockMvc.perform(post("/profile/favorite-coins")
        .header(HttpHeaders.AUTHORIZATION, token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(favoriteCoinJson("ethereum", "eth")))
      .andExpect(status().isCreated());

    mockMvc.perform(delete("/profile/favorite-coins/{coinId}", "ethereum")
        .header(HttpHeaders.AUTHORIZATION, token))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[?(@.coinId == 'ethereum')]").doesNotExist());
  }

  @Test
  @DisplayName("GET /profile/favorite-coins?prediction=true deve retornar moedas com predição")
  void shouldReturnFavoriteCoinsWithPrediction() throws Exception {
    Profile profile = seedUser("prediction@xchange.com");
    String token = bearerTokenFor(profile.getId());

    mockMvc.perform(post("/profile/favorite-coins")
        .header(HttpHeaders.AUTHORIZATION, token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(favoriteCoinJson("bitcoin", "btc")))
      .andExpect(status().isCreated());

    CoinPrediction prediction = new CoinPrediction();
    prediction.setSymbol("btc");
    prediction.setPrediction("UP");
    when(recommendationCoinServicePort.getCoinPredictionBySymbols(anyList()))
      .thenReturn(Set.of(prediction));

    mockMvc.perform(get("/profile/favorite-coins")
        .param("prediction", "true")
        .header(HttpHeaders.AUTHORIZATION, token))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].coinId").value("bitcoin"))
      .andExpect(jsonPath("$[0].prediction").value("UP"));
  }

  @Test
  @DisplayName("Deve retornar 401 quando o id do token não corresponde a um usuário existente")
  void shouldRejectWhenUserDoesNotExist() throws Exception {
    mockMvc.perform(get("/profile/details")
        .header(HttpHeaders.AUTHORIZATION, bearerTokenFor(UUID.randomUUID())))
      .andExpect(status().isUnauthorized());
  }
}
