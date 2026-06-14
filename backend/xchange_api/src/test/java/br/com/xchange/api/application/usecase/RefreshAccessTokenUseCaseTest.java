package br.com.xchange.api.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.xchange.api.application.dto.response.LoginResponseDto;
import br.com.xchange.api.domain.entities.RefreshToken;
import br.com.xchange.api.domain.ports.services.AccessTokenServicePort;
import br.com.xchange.api.domain.ports.services.RefreshTokenServicePort;

@ExtendWith(MockitoExtension.class)
class RefreshAccessTokenUseCaseTest {

  @Mock
  private RefreshTokenServicePort refreshTokenServicePort;

  @Mock
  private AccessTokenServicePort accessTokenServicePort;

  @InjectMocks
  private RefreshAccessTokenUseCase useCase;

  @Test
  @DisplayName("Deve validar o refresh token e gerar um novo access token")
  void shouldValidateRefreshTokenAndGenerateAccessToken() {
    UUID userId = UUID.randomUUID();
    UUID refreshTokenId = UUID.randomUUID();

    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setId(refreshTokenId);
    refreshToken.setUserId(userId);

    when(refreshTokenServicePort.validate(refreshTokenId)).thenReturn(refreshToken);
    when(accessTokenServicePort.generateFromUserId(userId)).thenReturn("new-access-token");

    LoginResponseDto response = useCase.execute(refreshTokenId);

    assertEquals("new-access-token", response.accessToken());
    assertEquals(refreshTokenId.toString(), response.refreshToken());
  }
}
