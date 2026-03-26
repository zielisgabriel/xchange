package br.com.xchange.api.application.usecase;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.xchange.api.application.dto.request.RegisterUserRequestDto;
import br.com.xchange.api.domain.entities.AuthUser;
import br.com.xchange.api.domain.exceptions.UserAlreadyExistsException;
import br.com.xchange.api.domain.ports.repositories.AuthUserRepositoryPort;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegisterUserUseCase {
  private final AuthUserRepositoryPort authUserRepositoryPort;
  private final PasswordEncoder passwordEncoder;

  public AuthUser execute(RegisterUserRequestDto requestDto) {
    this.authUserRepositoryPort.findByEmail(requestDto.email())
      .ifPresent(user -> {
        throw new UserAlreadyExistsException("Usuário já existe!");
      });

    String passwordHashed = passwordEncoder.encode(requestDto.password());

    AuthUser authUser = requestDto.toDomain();
    authUser.setPassword(passwordHashed);

    return this.authUserRepositoryPort.save(authUser);
  }
}
