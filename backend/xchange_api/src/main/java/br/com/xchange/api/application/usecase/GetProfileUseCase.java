package br.com.xchange.api.application.usecase;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.xchange.api.domain.entities.Profile;
import br.com.xchange.api.domain.exceptions.InvalidUserException;
import br.com.xchange.api.domain.ports.repositories.ProfileRepositoryPort;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetProfileUseCase {
  private final ProfileRepositoryPort repositoryPort;

  public Profile execute(UUID userId) {
    return this.repositoryPort.findById(userId)
      .orElseThrow(() -> new InvalidUserException());
  }
}
