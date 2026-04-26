package br.com.xchange.api.application.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.xchange.api.application.dto.request.RegisterUserRequestDto;
import br.com.xchange.api.domain.entities.AuthUser;
import br.com.xchange.api.domain.exceptions.UserAlreadyExistsException;
import br.com.xchange.api.domain.ports.repositories.AuthUserRepositoryPort;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseTest {

  @Mock
  private AuthUserRepositoryPort authUserRepositoryPort;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private RegisterUserUseCase registerUserUseCase;

  private static final String VALID_CPF = "52998224725";
  private static final String TEST_EMAIL = "john@example.com";
  private static final String TEST_FIRST_NAME = "John";
  private static final String TEST_LAST_NAME = "Doe";
  private static final String TEST_PASSWORD = "securePassword123";
  private static final String HASHED_PASSWORD = "$2a$10$hashedPasswordValue";
  private static final LocalDate TEST_BIRTH_DATE = LocalDate.of(1995, 6, 15);

  private RegisterUserRequestDto validRequestDto;

  @BeforeEach
  void setUp() {
    validRequestDto = new RegisterUserRequestDto(
        TEST_EMAIL,
        VALID_CPF,
        TEST_FIRST_NAME,
        TEST_LAST_NAME,
        TEST_PASSWORD,
        TEST_BIRTH_DATE
    );
  }

  @Nested
  @DisplayName("Cenários de sucesso")
  class SuccessScenarios {

    @Test
    @DisplayName("Deve registrar um novo usuário com sucesso")
    void shouldRegisterNewUserSuccessfully() {
      when(authUserRepositoryPort.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());
      when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn(HASHED_PASSWORD);

      AuthUser savedUser = new AuthUser();
      savedUser.setId(UUID.randomUUID());
      savedUser.setEmail(TEST_EMAIL);
      savedUser.setFirstName(TEST_FIRST_NAME);
      savedUser.setLastName(TEST_LAST_NAME);
      savedUser.setPassword(HASHED_PASSWORD);
      when(authUserRepositoryPort.save(any(AuthUser.class))).thenReturn(savedUser);

      AuthUser result = registerUserUseCase.execute(validRequestDto);

      assertNotNull(result);
      assertEquals(TEST_EMAIL, result.getEmail());
      assertEquals(TEST_FIRST_NAME, result.getFirstName());
      assertEquals(TEST_LAST_NAME, result.getLastName());
      assertEquals(HASHED_PASSWORD, result.getPassword());
    }

    @Test
    @DisplayName("Deve verificar se o e-mail já existe antes de registrar")
    void shouldCheckIfEmailExistsBeforeRegistering() {
      when(authUserRepositoryPort.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());
      when(passwordEncoder.encode(anyString())).thenReturn(HASHED_PASSWORD);
      when(authUserRepositoryPort.save(any(AuthUser.class))).thenReturn(new AuthUser());

      registerUserUseCase.execute(validRequestDto);

      verify(authUserRepositoryPort).findByEmail(TEST_EMAIL);
    }

    @Test
    @DisplayName("Deve codificar a senha antes de salvar")
    void shouldEncodePasswordBeforeSaving() {
      when(authUserRepositoryPort.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());
      when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn(HASHED_PASSWORD);
      when(authUserRepositoryPort.save(any(AuthUser.class))).thenReturn(new AuthUser());

      registerUserUseCase.execute(validRequestDto);

      verify(passwordEncoder).encode(TEST_PASSWORD);
    }

    @Test
    @DisplayName("Deve salvar o usuário com a senha codificada, não a senha original")
    void shouldSaveUserWithHashedPassword() {
      when(authUserRepositoryPort.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());
      when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn(HASHED_PASSWORD);
      when(authUserRepositoryPort.save(any(AuthUser.class))).thenAnswer(invocation -> invocation.getArgument(0));

      registerUserUseCase.execute(validRequestDto);

      ArgumentCaptor<AuthUser> captor = ArgumentCaptor.forClass(AuthUser.class);
      verify(authUserRepositoryPort).save(captor.capture());

      AuthUser capturedUser = captor.getValue();
      assertEquals(HASHED_PASSWORD, capturedUser.getPassword());
      assertNotEquals(TEST_PASSWORD, capturedUser.getPassword());
    }

    @Test
    @DisplayName("Deve mapear corretamente os campos do DTO para a entidade de domínio")
    void shouldMapDtoFieldsToEntityCorrectly() {
      when(authUserRepositoryPort.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());
      when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn(HASHED_PASSWORD);
      when(authUserRepositoryPort.save(any(AuthUser.class))).thenAnswer(invocation -> invocation.getArgument(0));

      registerUserUseCase.execute(validRequestDto);

      ArgumentCaptor<AuthUser> captor = ArgumentCaptor.forClass(AuthUser.class);
      verify(authUserRepositoryPort).save(captor.capture());

      AuthUser capturedUser = captor.getValue();
      assertEquals(TEST_EMAIL, capturedUser.getEmail());
      assertEquals(TEST_FIRST_NAME, capturedUser.getFirstName());
      assertEquals(TEST_LAST_NAME, capturedUser.getLastName());
      assertEquals(VALID_CPF, capturedUser.getCpf().getValue());
      assertEquals(TEST_BIRTH_DATE, capturedUser.getBirthDate().getValue());
    }
  }

  @Nested
  @DisplayName("Cenários de falha")
  class FailureScenarios {

    @Test
    @DisplayName("Deve lançar UserAlreadyExistsException quando o e-mail já existe")
    void shouldThrowWhenUserAlreadyExists() {
      AuthUser existingUser = new AuthUser();
      existingUser.setEmail(TEST_EMAIL);
      when(authUserRepositoryPort.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(existingUser));

      UserAlreadyExistsException exception = assertThrows(
          UserAlreadyExistsException.class,
          () -> registerUserUseCase.execute(validRequestDto)
      );

      assertEquals("Usuário já existe!", exception.getMessage());
    }

    @Test
    @DisplayName("Não deve codificar a senha quando o usuário já existe")
    void shouldNotEncodePasswordWhenUserAlreadyExists() {
      AuthUser existingUser = new AuthUser();
      existingUser.setEmail(TEST_EMAIL);
      when(authUserRepositoryPort.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(existingUser));

      assertThrows(UserAlreadyExistsException.class,
          () -> registerUserUseCase.execute(validRequestDto));

      verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    @DisplayName("Não deve salvar o usuário quando o e-mail já existe")
    void shouldNotSaveUserWhenUserAlreadyExists() {
      AuthUser existingUser = new AuthUser();
      existingUser.setEmail(TEST_EMAIL);
      when(authUserRepositoryPort.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(existingUser));

      assertThrows(UserAlreadyExistsException.class,
          () -> registerUserUseCase.execute(validRequestDto));

      verify(authUserRepositoryPort, never()).save(any(AuthUser.class));
    }
  }

  @Nested
  @DisplayName("Ordem de execução")
  class ExecutionOrder {

    @Test
    @DisplayName("Deve executar na ordem: verificar e-mail → codificar senha → salvar")
    void shouldExecuteInCorrectOrder() {
      when(authUserRepositoryPort.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());
      when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn(HASHED_PASSWORD);
      when(authUserRepositoryPort.save(any(AuthUser.class))).thenReturn(new AuthUser());

      InOrder inOrder = inOrder(authUserRepositoryPort, passwordEncoder);

      registerUserUseCase.execute(validRequestDto);

      inOrder.verify(authUserRepositoryPort).findByEmail(TEST_EMAIL);
      inOrder.verify(passwordEncoder).encode(TEST_PASSWORD);
      inOrder.verify(authUserRepositoryPort).save(any(AuthUser.class));
    }
  }
}
