package br.com.xchange.api.e2e;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.jayway.jsonpath.JsonPath;

import br.com.xchange.api.domain.ports.services.CoinServicePort;
import br.com.xchange.api.domain.ports.services.RecommendationCoinServicePort;
import br.com.xchange.api.infra.adapters.implementations.JpaAuthUserRepositoryImpl;
import br.com.xchange.api.infra.adapters.implementations.JpaProfileRepositoryImpl;
import br.com.xchange.api.infra.adapters.implementations.RedisRefreshTokenRepositoryImpl;
import br.com.xchange.api.infra.entities.RefreshTokenRedis;

/**
 * Base para os testes ponta a ponta (E2E).
 *
 * Diferente dos testes de integração, aqui o servidor sobe numa porta HTTP real
 * ({@code WebEnvironment.RANDOM_PORT}) e os cenários batem nele com um cliente
 * HTTP real (o {@link HttpClient} do JDK). As jornadas são encadeadas usando os
 * tokens que o próprio servidor emite (login → access token → refresh token).
 *
 * Como o servidor roda em outra thread, não há transação compartilhada nem
 * rollback: cada teste limpa o banco no início ({@link #resetState()}).
 *
 * Os limites externos continuam mockados — CoinGecko, IA e o Redis de refresh
 * tokens (este último com um estado em memória para que a jornada de refresh
 * funcione de verdade entre as chamadas HTTP).
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({ "dev", "test" })
abstract class AbstractE2ETest {

  protected static final String VALID_CPF = "11144477735";
  protected static final String DEFAULT_PASSWORD = "password123";

  @LocalServerPort
  protected int port;

  private final HttpClient httpClient = HttpClient.newHttpClient();

  @Autowired
  private JpaProfileRepositoryImpl profileRepository;

  @Autowired
  private JpaAuthUserRepositoryImpl authUserRepository;

  @MockitoBean
  protected CoinServicePort coinServicePort;

  @MockitoBean
  protected RecommendationCoinServicePort recommendationCoinServicePort;

  @MockitoBean
  protected RedisRefreshTokenRepositoryImpl redisRefreshTokenRepository;

  /** Estado em memória que faz o mock do Redis se comportar como um store real. */
  private final Map<UUID, RefreshTokenRedis> refreshTokenStore = new ConcurrentHashMap<>();

  @BeforeEach
  void resetState() {
    this.refreshTokenStore.clear();
    this.profileRepository.deleteAll();
    this.authUserRepository.deleteAll();

    when(this.redisRefreshTokenRepository.save(any(RefreshTokenRedis.class)))
      .thenAnswer(invocation -> {
        RefreshTokenRedis token = invocation.getArgument(0);
        this.refreshTokenStore.put(token.getId(), token);
        return token;
      });

    when(this.redisRefreshTokenRepository.findById(any()))
      .thenAnswer(invocation -> Optional.ofNullable(this.refreshTokenStore.get(invocation.getArgument(0))));

    when(this.redisRefreshTokenRepository.findByUserId(any()))
      .thenAnswer(invocation -> {
        UUID userId = invocation.getArgument(0);
        return this.refreshTokenStore.values().stream()
          .filter(token -> userId.equals(token.getUserId()))
          .findFirst();
      });
  }

  protected String url(String path) {
    return "http://localhost:" + this.port + path;
  }

  protected HttpResult exchange(String method, String path, String jsonBody, String bearerToken) {
    HttpRequest.BodyPublisher body = jsonBody == null
      ? HttpRequest.BodyPublishers.noBody()
      : HttpRequest.BodyPublishers.ofString(jsonBody);

    HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(url(path)))
      .method(method, body);

    if (jsonBody != null) {
      builder.header("Content-Type", "application/json");
    }
    if (bearerToken != null) {
      builder.header("Authorization", "Bearer " + bearerToken);
    }

    try {
      HttpResponse<String> response = this.httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
      return new HttpResult(response.statusCode(), response.body());
    } catch (IOException | InterruptedException exception) {
      throw new RuntimeException(exception);
    }
  }

  protected HttpResult get(String path, String bearerToken) {
    return exchange("GET", path, null, bearerToken);
  }

  protected HttpResult post(String path, String jsonBody, String bearerToken) {
    return exchange("POST", path, jsonBody, bearerToken);
  }

  protected HttpResult delete(String path, String bearerToken) {
    return exchange("DELETE", path, null, bearerToken);
  }

  protected String registerPayload(String email) {
    return """
      {
        "email": "%s",
        "cpf": "%s",
        "first_name": "Jose",
        "last_name": "Silva",
        "password": "%s",
        "birth_date": "2000-01-01"
      }
      """.formatted(email, VALID_CPF, DEFAULT_PASSWORD);
  }

  protected HttpResult register(String email) {
    return post("/auth/register", registerPayload(email), null);
  }

  protected Tokens login(String email, String password) {
    HttpResult response = post("/auth/login",
      "{ \"email\": \"%s\", \"password\": \"%s\" }".formatted(email, password), null);

    String access = JsonPath.read(response.body(), "$.access_token");
    String refresh = JsonPath.read(response.body(), "$.refresh_token");

    return new Tokens(access, refresh);
  }

  /** Registra um usuário e já devolve os tokens de uma sessão autenticada. */
  protected Tokens registerAndLogin(String email) {
    register(email);
    return login(email, DEFAULT_PASSWORD);
  }

  protected record HttpResult(int status, String body) {}

  protected record Tokens(String accessToken, String refreshToken) {}
}
