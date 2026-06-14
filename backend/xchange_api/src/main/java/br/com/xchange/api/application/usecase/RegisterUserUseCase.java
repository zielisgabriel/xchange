package br.com.xchange.api.application.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.xchange.api.application.dto.request.RegisterUserRequestDto;
import br.com.xchange.api.domain.entities.AuthUser;
import br.com.xchange.api.domain.entities.Profile;
import br.com.xchange.api.domain.exceptions.UserAlreadyExistsException;
import br.com.xchange.api.domain.ports.repositories.AuthUserRepositoryPort;
import br.com.xchange.api.domain.ports.repositories.ProfileRepositoryPort;
import br.com.xchange.api.domain.ports.services.PasswordEncoderPort;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegisterUserUseCase {
  private final AuthUserRepositoryPort authUserRepositoryPort;
  private final ProfileRepositoryPort profileRepositoryPort;
  private final PasswordEncoderPort passwordEncoder;

  @Transactional
  public Profile execute(RegisterUserRequestDto requestDto) {
    this.authUserRepositoryPort.findByEmail(requestDto.email())
      .ifPresent(user -> {
        throw new UserAlreadyExistsException("Usuário já existe!");
      });

    String passwordHashed = passwordEncoder.encode(requestDto.password());

    AuthUser authUser = requestDto.toDomain();
    authUser.setPassword(passwordHashed);

    authUser = this.authUserRepositoryPort.save(authUser);

    Profile profile = new Profile();
    profile.setAuthUser(authUser);
    
    return this.profileRepositoryPort.save(profile);
  }
}
