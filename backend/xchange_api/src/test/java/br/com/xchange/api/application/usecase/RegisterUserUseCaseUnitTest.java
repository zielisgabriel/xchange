package br.com.xchange.api.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.xchange.api.application.dto.request.RegisterUserRequestDto;
import br.com.xchange.api.domain.entities.AuthUser;
import br.com.xchange.api.domain.entities.Profile;
import br.com.xchange.api.domain.exceptions.UserAlreadyExistsException;
import br.com.xchange.api.domain.ports.repositories.AuthUserRepositoryPort;
import br.com.xchange.api.domain.ports.repositories.ProfileRepositoryPort;
import br.com.xchange.api.domain.ports.services.PasswordEncoderPort;

@ExtendWith(MockitoExtension.class)
public class RegisterUserUseCaseUnitTest {
  @Mock
  private AuthUserRepositoryPort authUserRepositoryPort;
  
  @Mock
  private ProfileRepositoryPort profileRepositoryPort;

  @Mock
  private PasswordEncoderPort passwordEncoder;

  @InjectMocks
  private RegisterUserUseCase useCase;

  @Captor
  private ArgumentCaptor<AuthUser> authUserCaptor;

  @Captor
  private ArgumentCaptor<Profile> profileCaptor;

  private RegisterUserRequestDto requestDto;

  @BeforeEach
  void init() {
    requestDto = new RegisterUserRequestDto(
      "emailtest@gmail.com",
      "12345678909",
      "First Name Test",
      "Last Name Test",
      "password@123",
      LocalDate.of(2005, 8, 20) // fixed year to avoid issues
    );
  }
  
  @Test
  @DisplayName("Must create a new user")
  void mustCreateANewUser() {
    // 1. Arrange
    AuthUser mockedAuthUser = requestDto.toDomain();
    mockedAuthUser.setPassword("password-encoded");

    Profile mockedProfile = new Profile();
    mockedProfile.setAuthUser(mockedAuthUser);

    when(this.authUserRepositoryPort.findByEmail(requestDto.email())).thenReturn(Optional.empty());
    when(this.passwordEncoder.encode(requestDto.password())).thenReturn("password-encoded");
    
    // We use any() here because the use case instantiates and modifies the entities internally
    when(this.authUserRepositoryPort.save(any(AuthUser.class))).thenReturn(mockedAuthUser);
    when(this.profileRepositoryPort.save(any(Profile.class))).thenReturn(mockedProfile);

    // 2. Act
    Profile result = this.useCase.execute(requestDto);

    // 3. Assert
    assertNotNull(result);
    assertEquals("password-encoded", result.getAuthUser().getPassword());

    // Verify AuthUser save
    verify(this.authUserRepositoryPort).save(authUserCaptor.capture());
    AuthUser capturedAuthUser = authUserCaptor.getValue();
    assertEquals(requestDto.email(), capturedAuthUser.getEmail());
    assertEquals("password-encoded", capturedAuthUser.getPassword());

    // Verify Profile save
    verify(this.profileRepositoryPort).save(profileCaptor.capture());
    Profile capturedProfile = profileCaptor.getValue();
    assertEquals(capturedAuthUser, capturedProfile.getAuthUser());
  }

  @Test
  @DisplayName("Must not create a user with email already exists")
  void mustNotCreateAUserWithEmailAlreadyExists() {
    when(this.authUserRepositoryPort.findByEmail(requestDto.email())).thenReturn(Optional.of(requestDto.toDomain()));

    assertThrows(UserAlreadyExistsException.class, () -> this.useCase.execute(requestDto));
  }
}
