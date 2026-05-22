package br.com.xchange.api.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.xchange.api.domain.entities.RefreshToken;
import br.com.xchange.api.domain.exceptions.RefreshTokenAlreadyExistsException;
import br.com.xchange.api.domain.ports.repositories.RefreshTokenRepositoryPort;

@ExtendWith(MockitoExtension.class)
public class CreateRefreshTokenUseCaseUnitTest {
  @Mock
  private RefreshTokenRepositoryPort repository;

  @InjectMocks
  private CreateRefreshTokenUseCase createRefreshTokenUseCase;

  @Captor
  private ArgumentCaptor<RefreshToken> refreshTokenCaptor;

  private UUID userId;
  private RefreshToken refreshToken;

  @BeforeEach
  void init() {
    userId = UUID.randomUUID();
    refreshToken = new RefreshToken();
    refreshToken.setUserId(userId);
    refreshToken.setId(UUID.randomUUID());
    refreshToken.setExpiration(Duration.ofHours(10).getSeconds());
  }

  @Test
  @DisplayName("Must create a new refresh token")
  void mustCreateARefreshToken() {
    when(this.repository.findByUserId(userId)).thenReturn(Optional.empty());
    when(this.repository.save(any(RefreshToken.class))).thenReturn(refreshToken);

    RefreshToken createdToken = this.createRefreshTokenUseCase.execute(userId);

    assertNotNull(createdToken);
    assertEquals(userId, createdToken.getUserId());
    
    verify(this.repository).save(refreshTokenCaptor.capture());
    RefreshToken capturedToken = refreshTokenCaptor.getValue();
    assertEquals(userId, capturedToken.getUserId());
  }

  @Test
  @DisplayName("Must not create a refresh token with userId already exists")
  void mustNotCreateARefreshTokenWithUserIdAlreadyExists() {
    when(this.repository.findByUserId(userId)).thenReturn(Optional.of(refreshToken));
    
    assertThrows(RefreshTokenAlreadyExistsException.class, () -> this.createRefreshTokenUseCase.execute(userId));
  }
}
