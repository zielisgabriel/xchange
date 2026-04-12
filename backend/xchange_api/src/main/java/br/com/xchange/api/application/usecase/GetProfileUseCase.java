package br.com.xchange.api.application.usecase;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.xchange.api.domain.entities.AuthUser;
import br.com.xchange.api.domain.exceptions.UserNotFoundException;
import br.com.xchange.api.domain.ports.repositories.AuthUserRepositoryPort;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetProfileUseCase {
  private final AuthUserRepositoryPort repositoryPort;

  public AuthUser execute(UUID userId) {
    return this.repositoryPort.findById(userId)
      .orElseThrow(() -> new UserNotFoundException());
  }
}
