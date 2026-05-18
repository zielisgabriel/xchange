package br.com.xchange.api.application.usecase;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.xchange.api.domain.entities.RefreshToken;
import br.com.xchange.api.domain.exceptions.RefreshTokenAlreadyExistsException;
import br.com.xchange.api.domain.ports.repositories.RefreshTokenRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateRefreshTokenUseCase {
  private final RefreshTokenRepositoryPort repository;

  public RefreshToken execute(UUID userId) {
    this.repository.findByUserId(userId)
      .ifPresent(refreshToken -> {
        throw new RefreshTokenAlreadyExistsException();
      });

    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setUserId(userId);
    
    return this.repository.save(refreshToken);
  }
}
