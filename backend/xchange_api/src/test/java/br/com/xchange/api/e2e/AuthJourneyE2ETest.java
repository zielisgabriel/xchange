package br.com.xchange.api.e2e;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jayway.jsonpath.JsonPath;

@DisplayName("E2E - Jornada de autenticação")
class AuthJourneyE2ETest extends AbstractE2ETest {

  @Test
  @DisplayName("Deve completar registro → login → acesso protegido → refresh → novo acesso")
  void shouldCompleteFullAuthenticationJourney() {
    String email = "jornada@xchange.com";

    // 1. Registro
    HttpResult register = register(email);
    assertEquals(201, register.status());

    // 2. Login devolve tokens emitidos pelo servidor
    Tokens tokens = login(email, DEFAULT_PASSWORD);
    assertNotNull(tokens.accessToken());
    assertNotNull(tokens.refreshToken());

    // 3. O access token dá acesso a um recurso protegido
    HttpResult profile = get("/profile/details", tokens.accessToken());
    assertEquals(200, profile.status());
    assertEquals(email, JsonPath.read(profile.body(), "$.authUser.email"));

    // 4. O refresh token gera um novo access token
    HttpResult refreshed = post("/auth/refresh",
      "{ \"refresh_token\": \"%s\" }".formatted(tokens.refreshToken()), null);
    assertEquals(200, refreshed.status());
    String newAccessToken = JsonPath.read(refreshed.body(), "$.access_token");
    assertNotNull(newAccessToken);

    // 5. O novo access token também acessa o recurso protegido
    HttpResult profileAgain = get("/profile/details", newAccessToken);
    assertEquals(200, profileAgain.status());
  }

  @Test
  @DisplayName("Deve negar acesso a recurso protegido sem token (401)")
  void shouldRejectProtectedResourceWithoutToken() {
    HttpResult response = get("/profile/details", null);

    assertEquals(401, response.status());
  }

  @Test
  @DisplayName("Deve rejeitar login com senha incorreta (409)")
  void shouldRejectLoginWithWrongPassword() {
    String email = "senhaerrada@xchange.com";
    register(email);

    HttpResult response = post("/auth/login",
      "{ \"email\": \"%s\", \"password\": \"senha-errada\" }".formatted(email), null);

    assertEquals(409, response.status());
  }

  @Test
  @DisplayName("Deve rejeitar registro duplicado do mesmo e-mail (409)")
  void shouldRejectDuplicateRegistration() {
    String email = "duplicado@xchange.com";

    assertEquals(201, register(email).status());
    assertEquals(409, register(email).status());
  }

  @Test
  @DisplayName("Deve rejeitar refresh com token inexistente (401)")
  void shouldRejectUnknownRefreshToken() {
    HttpResult response = post("/auth/refresh",
      "{ \"refresh_token\": \"%s\" }".formatted(UUID.randomUUID()), null);

    assertEquals(401, response.status());
  }
}
