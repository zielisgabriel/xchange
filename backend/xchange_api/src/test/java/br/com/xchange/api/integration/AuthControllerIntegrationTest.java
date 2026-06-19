package br.com.xchange.api.integration;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import br.com.xchange.api.domain.entities.Profile;
import br.com.xchange.api.infra.adapters.implementations.JpaAuthUserRepositoryImpl;
import br.com.xchange.api.infra.entities.RefreshTokenRedis;

@DisplayName("Integração - AuthController")
class AuthControllerIntegrationTest extends AbstractIntegrationTest {

  @Autowired
  private JpaAuthUserRepositoryImpl authUserRepository;

  private String registerPayload(String email) {
    return """
      {
        "email": "%s",
        "cpf": "%s",
        "first_name": "Maria",
        "last_name": "Souza",
        "password": "password123",
        "birth_date": "2000-01-01"
      }
      """.formatted(email, VALID_CPF);
  }

  @Nested
  @DisplayName("POST /auth/register")
  class Register {

    @Test
    @DisplayName("Deve registrar um usuário e persistir no banco retornando 201")
    void shouldRegisterUser() throws Exception {
      String email = "novo@xchange.com";

      mockMvc.perform(post("/auth/register")
          .contentType(MediaType.APPLICATION_JSON)
          .content(registerPayload(email)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.authUser.email").value(email))
        .andExpect(jsonPath("$.authUser.firstName").value("Maria"))
        .andExpect(jsonPath("$.favoriteCoins").isArray());

      assertTrue(authUserRepository.findByEmail(email).isPresent());
    }

    @Test
    @DisplayName("Deve retornar 409 ao tentar registrar e-mail já existente")
    void shouldRejectDuplicatedEmail() throws Exception {
      String email = "dup@xchange.com";

      mockMvc.perform(post("/auth/register")
          .contentType(MediaType.APPLICATION_JSON)
          .content(registerPayload(email)))
        .andExpect(status().isCreated());

      mockMvc.perform(post("/auth/register")
          .contentType(MediaType.APPLICATION_JSON)
          .content(registerPayload(email)))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.message").value("Usuário já existe!"));
    }

    @Test
    @DisplayName("Deve retornar 422 quando o corpo é inválido")
    void shouldRejectInvalidBody() throws Exception {
      String invalidPayload = """
        {
          "email": "not-an-email",
          "cpf": "123",
          "first_name": "M",
          "last_name": "S",
          "password": "short",
          "birth_date": "2000-01-01"
        }
        """;

      mockMvc.perform(post("/auth/register")
          .contentType(MediaType.APPLICATION_JSON)
          .content(invalidPayload))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("Erro de validação!"))
        .andExpect(jsonPath("$.fieldErrors").isArray());
    }
  }

  @Nested
  @DisplayName("POST /auth/login")
  class Login {

    @Test
    @DisplayName("Deve autenticar e retornar access e refresh tokens com 200")
    void shouldLoginSuccessfully() throws Exception {
      seedUser("login@xchange.com");
      when(redisRefreshTokenRepository.findByUserId(any())).thenReturn(Optional.empty());
      when(redisRefreshTokenRepository.save(any(RefreshTokenRedis.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

      String loginPayload = """
        { "email": "login@xchange.com", "password": "password123" }
        """;

      mockMvc.perform(post("/auth/login")
          .contentType(MediaType.APPLICATION_JSON)
          .content(loginPayload))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.access_token").exists())
        .andExpect(jsonPath("$.refresh_token").exists());
    }

    @Test
    @DisplayName("Deve retornar 409 quando a senha está incorreta")
    void shouldRejectWrongPassword() throws Exception {
      seedUser("wrongpass@xchange.com");

      String loginPayload = """
        { "email": "wrongpass@xchange.com", "password": "wrongpassword" }
        """;

      mockMvc.perform(post("/auth/login")
          .contentType(MediaType.APPLICATION_JSON)
          .content(loginPayload))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.message").value("Email ou senha inválida!"));
    }

    @Test
    @DisplayName("Deve retornar 409 quando o usuário não existe")
    void shouldRejectUnknownUser() throws Exception {
      String loginPayload = """
        { "email": "ghost@xchange.com", "password": "password123" }
        """;

      mockMvc.perform(post("/auth/login")
          .contentType(MediaType.APPLICATION_JSON)
          .content(loginPayload))
        .andExpect(status().isConflict());
    }
  }

  @Nested
  @DisplayName("POST /auth/refresh")
  class Refresh {

    @Test
    @DisplayName("Deve emitir novo access token a partir de um refresh token válido")
    void shouldRefreshSuccessfully() throws Exception {
      Profile profile = seedUser("refresh@xchange.com");
      UUID userId = profile.getId();
      UUID refreshTokenId = UUID.randomUUID();

      RefreshTokenRedis stored = new RefreshTokenRedis();
      stored.setId(refreshTokenId);
      stored.setUserId(userId);
      when(redisRefreshTokenRepository.findById(refreshTokenId)).thenReturn(Optional.of(stored));

      String payload = """
        { "refresh_token": "%s" }
        """.formatted(refreshTokenId);

      mockMvc.perform(post("/auth/refresh")
          .contentType(MediaType.APPLICATION_JSON)
          .content(payload))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.access_token").exists())
        .andExpect(jsonPath("$.refresh_token").value(refreshTokenId.toString()));
    }

    @Test
    @DisplayName("Deve retornar 401 quando o refresh token não existe")
    void shouldRejectUnknownRefreshToken() throws Exception {
      when(redisRefreshTokenRepository.findById(any())).thenReturn(Optional.empty());

      String payload = """
        { "refresh_token": "%s" }
        """.formatted(UUID.randomUUID());

      mockMvc.perform(post("/auth/refresh")
          .contentType(MediaType.APPLICATION_JSON)
          .content(payload))
        .andExpect(status().isUnauthorized());
    }
  }
}
