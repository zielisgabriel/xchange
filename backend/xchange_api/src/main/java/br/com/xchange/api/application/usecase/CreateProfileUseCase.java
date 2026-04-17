package br.com.xchange.api.application.usecase;

import org.springframework.stereotype.Service;

import br.com.xchange.api.application.dto.request.CreateProfileRequestDto;
import br.com.xchange.api.domain.entities.Profile;
import br.com.xchange.api.domain.exceptions.UserAlreadyExistsException;
import br.com.xchange.api.domain.ports.repositories.ProfileRepositoryPort;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateProfileUseCase {
  private final ProfileRepositoryPort repositoryPort;

  public Profile execute(CreateProfileRequestDto requestDto) {
    this.repositoryPort.findById(requestDto.userId())
      .ifPresent((user) -> {
        throw new UserAlreadyExistsException("Perfil já existe");
      });

    Profile profile = requestDto.toDomain();

    return this.repositoryPort.save(profile);
  }
}
