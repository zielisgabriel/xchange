package br.com.xchange.api.integration;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import br.com.xchange.api.application.dto.request.RegisterUserRequest;
import br.com.xchange.api.application.usecase.RegisterUserUseCase;
import br.com.xchange.api.domain.entities.Profile;
import br.com.xchange.api.domain.ports.services.AccessTokenServicePort;
import br.com.xchange.api.domain.ports.services.CoinServicePort;
import br.com.xchange.api.domain.ports.services.RecommendationCoinServicePort;
import br.com.xchange.api.infra.adapters.implementations.RedisRefreshTokenRepositoryImpl;

/**
 * Base para os testes de integração HTTP de ponta a ponta.
 *
 * Sobe o contexto completo do Spring ({@code @SpringBootTest}) com banco H2 em
 * memória e segurança/JWT reais. Apenas os limites externos são mockados:
 * CoinGecko ({@link CoinServicePort}), o serviço de IA
 * ({@link RecommendationCoinServicePort}) e o Redis de refresh tokens
 * ({@link RedisRefreshTokenRepositoryImpl}).
 *
 * Cada teste roda dentro de uma transação revertida ao final, garantindo
 * isolamento do banco entre cenários.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({ "dev", "test" })
@Transactional
abstract class AbstractIntegrationTest {

  protected static final String VALID_CPF = "11144477735";
  protected static final LocalDate VALID_BIRTH_DATE = LocalDate.of(2000, 1, 1);

  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected RegisterUserUseCase registerUserUseCase;

  @Autowired
  protected AccessTokenServicePort accessTokenServicePort;

  @MockitoBean
  protected CoinServicePort coinServicePort;

  @MockitoBean
  protected RecommendationCoinServicePort recommendationCoinServicePort;

  @MockitoBean
  protected RedisRefreshTokenRepositoryImpl redisRefreshTokenRepository;

  /**
   * Persiste um usuário válido e devolve o perfil resultante (cujo id é o
   * mesmo id do usuário, por conta do {@code @MapsId}).
   */
  protected Profile seedUser(String email) {
    RegisterUserRequest request = new RegisterUserRequest(
      email,
      VALID_CPF,
      "Jose",
      "Silva",
      "password123",
      VALID_BIRTH_DATE
    );

    return this.registerUserUseCase.execute(request);
  }

  /** Gera um access token JWT válido para o id de usuário informado. */
  protected String bearerTokenFor(UUID userId) {
    return "Bearer " + this.accessTokenServicePort.generateFromUserId(userId);
  }
}
