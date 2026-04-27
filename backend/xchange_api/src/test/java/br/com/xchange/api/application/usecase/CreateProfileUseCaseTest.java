package br.com.xchange.api.application.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.xchange.api.application.dto.request.CreateProfileRequestDto;
import br.com.xchange.api.domain.entities.Profile;
import br.com.xchange.api.domain.exceptions.UserAlreadyExistsException;
import br.com.xchange.api.domain.ports.repositories.ProfileRepositoryPort;

@ExtendWith(MockitoExtension.class)
class CreateProfileUseCaseTest {

  @Mock
  private ProfileRepositoryPort repositoryPort;

  @InjectMocks
  private CreateProfileUseCase createProfileUseCase;

  private static final UUID TEST_USER_ID = UUID.randomUUID();
  private static final Set<String> TEST_FAVORITE_CRYPTOS = Set.of("bitcoin", "ethereum");

  private CreateProfileRequestDto validRequestDto;

  @BeforeEach
  void setUp() {
    validRequestDto = new CreateProfileRequestDto(TEST_USER_ID, TEST_FAVORITE_CRYPTOS);
  }

  @Nested
  @DisplayName("Cenários de sucesso")
  class SuccessScenarios {

    @Test
    @DisplayName("Deve criar um perfil com sucesso quando não existe")
    void shouldCreateProfileSuccessfully() {
      when(repositoryPort.findById(TEST_USER_ID)).thenReturn(Optional.empty());

      Profile savedProfile = new Profile();
      savedProfile.setId(TEST_USER_ID);
      savedProfile.setFavoriteCryptos(TEST_FAVORITE_CRYPTOS);
      when(repositoryPort.save(any(Profile.class))).thenReturn(savedProfile);

      Profile result = createProfileUseCase.execute(validRequestDto);

      assertNotNull(result);
      assertEquals(TEST_USER_ID, result.getId());
      assertEquals(TEST_FAVORITE_CRYPTOS, result.getFavoriteCryptos());
    }

    @Test
    @DisplayName("Deve verificar se o perfil já existe antes de criar")
    void shouldCheckIfProfileExistsBeforeCreating() {
      when(repositoryPort.findById(TEST_USER_ID)).thenReturn(Optional.empty());
      when(repositoryPort.save(any(Profile.class))).thenReturn(new Profile());

      createProfileUseCase.execute(validRequestDto);

      verify(repositoryPort).findById(TEST_USER_ID);
    }

    @Test
    @DisplayName("Deve mapear corretamente os campos do DTO para a entidade de domínio")
    void shouldMapDtoFieldsToEntityCorrectly() {
      when(repositoryPort.findById(TEST_USER_ID)).thenReturn(Optional.empty());
      when(repositoryPort.save(any(Profile.class))).thenAnswer(invocation -> invocation.getArgument(0));

      createProfileUseCase.execute(validRequestDto);

      ArgumentCaptor<Profile> captor = ArgumentCaptor.forClass(Profile.class);
      verify(repositoryPort).save(captor.capture());

      Profile capturedProfile = captor.getValue();
      assertEquals(TEST_USER_ID, capturedProfile.getId());
      assertEquals(TEST_FAVORITE_CRYPTOS, capturedProfile.getFavoriteCryptos());
    }
  }

  @Nested
  @DisplayName("Cenários de falha")
  class FailureScenarios {

    @Test
    @DisplayName("Deve lançar UserAlreadyExistsException quando o perfil já existe")
    void shouldThrowWhenProfileAlreadyExists() {
      Profile existingProfile = new Profile();
      existingProfile.setId(TEST_USER_ID);
      when(repositoryPort.findById(TEST_USER_ID)).thenReturn(Optional.of(existingProfile));

      UserAlreadyExistsException exception = assertThrows(
          UserAlreadyExistsException.class,
          () -> createProfileUseCase.execute(validRequestDto)
      );

      assertEquals("Perfil já existe", exception.getMessage());
    }

    @Test
    @DisplayName("Não deve salvar o perfil quando ele já existe")
    void shouldNotSaveProfileWhenAlreadyExists() {
      Profile existingProfile = new Profile();
      existingProfile.setId(TEST_USER_ID);
      when(repositoryPort.findById(TEST_USER_ID)).thenReturn(Optional.of(existingProfile));

      assertThrows(UserAlreadyExistsException.class,
          () -> createProfileUseCase.execute(validRequestDto));

      verify(repositoryPort, never()).save(any(Profile.class));
    }
  }
}
