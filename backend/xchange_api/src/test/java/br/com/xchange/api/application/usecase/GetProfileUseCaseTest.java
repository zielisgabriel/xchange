package br.com.xchange.api.application.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.xchange.api.domain.entities.Profile;
import br.com.xchange.api.domain.exceptions.UserNotFoundException;
import br.com.xchange.api.domain.ports.repositories.ProfileRepositoryPort;

@ExtendWith(MockitoExtension.class)
class GetProfileUseCaseTest {

  @Mock
  private ProfileRepositoryPort repositoryPort;

  @InjectMocks
  private GetProfileUseCase getProfileUseCase;

  private static final UUID TEST_USER_ID = UUID.randomUUID();

  @Nested
  @DisplayName("Cenários de sucesso")
  class SuccessScenarios {

    @Test
    @DisplayName("Deve retornar o perfil quando encontrado")
    void shouldReturnProfileWhenFound() {
      Profile expectedProfile = new Profile();
      expectedProfile.setId(TEST_USER_ID);
      expectedProfile.setFavoriteCryptos(Set.of("bitcoin", "ethereum"));
      when(repositoryPort.findById(TEST_USER_ID)).thenReturn(Optional.of(expectedProfile));

      Profile result = getProfileUseCase.execute(TEST_USER_ID);

      assertNotNull(result);
      assertEquals(TEST_USER_ID, result.getId());
      assertEquals(Set.of("bitcoin", "ethereum"), result.getFavoriteCryptos());
    }

    @Test
    @DisplayName("Deve buscar o perfil pelo ID informado")
    void shouldSearchProfileByGivenId() {
      when(repositoryPort.findById(TEST_USER_ID)).thenReturn(Optional.of(new Profile()));

      getProfileUseCase.execute(TEST_USER_ID);

      verify(repositoryPort).findById(TEST_USER_ID);
    }
  }

  @Nested
  @DisplayName("Cenários de falha")
  class FailureScenarios {

    @Test
    @DisplayName("Deve lançar UserNotFoundException quando o perfil não existe")
    void shouldThrowWhenProfileNotFound() {
      when(repositoryPort.findById(TEST_USER_ID)).thenReturn(Optional.empty());

      UserNotFoundException exception = assertThrows(
          UserNotFoundException.class,
          () -> getProfileUseCase.execute(TEST_USER_ID)
      );

      assertEquals("Perfil não encontrado ou não existe.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar UserNotFoundException para UUID inexistente")
    void shouldThrowForNonExistentUuid() {
      UUID unknownId = UUID.randomUUID();
      when(repositoryPort.findById(unknownId)).thenReturn(Optional.empty());

      assertThrows(UserNotFoundException.class,
          () -> getProfileUseCase.execute(unknownId));

      verify(repositoryPort).findById(unknownId);
    }
  }
}
