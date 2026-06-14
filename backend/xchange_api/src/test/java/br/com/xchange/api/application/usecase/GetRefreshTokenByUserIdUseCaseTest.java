package br.com.xchange.api.application.usecase;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.xchange.api.domain.entities.RefreshToken;
import br.com.xchange.api.domain.exceptions.RefreshTokenNotFoundException;
import br.com.xchange.api.domain.ports.repositories.RefreshTokenRepositoryPort;

@ExtendWith(MockitoExtension.class)
class GetRefreshTokenByUserIdUseCaseTest {

  @Mock
  private RefreshTokenRepositoryPort repository;

  @InjectMocks
  private GetRefreshTokenByUserIdUseCase useCase;

  private static final UUID USER_ID = UUID.randomUUID();

  @Test
  @DisplayName("Deve retornar o refresh token do usuário")
  void shouldReturnRefreshToken() {
    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setUserId(USER_ID);
    when(repository.findByUserId(USER_ID)).thenReturn(Optional.of(refreshToken));

    RefreshToken result = useCase.execute(USER_ID);

    assertSame(refreshToken, result);
  }

  @Test
  @DisplayName("Deve lançar RefreshTokenNotFoundException quando não existe")
  void shouldThrowWhenNotFound() {
    when(repository.findByUserId(USER_ID)).thenReturn(Optional.empty());

    assertThrows(RefreshTokenNotFoundException.class, () -> useCase.execute(USER_ID));
  }
}
