package br.com.xchange.api.application.usecase;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.xchange.api.domain.entities.RefreshToken;
import br.com.xchange.api.domain.exceptions.RefreshTokenNotFoundException;
import br.com.xchange.api.domain.ports.repositories.RefreshTokenRepositoryPort;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetRefreshTokenByUserIdUseCase {
  private final RefreshTokenRepositoryPort repository;

  public RefreshToken execute(UUID userId) {
    RefreshToken refreshToken = this.repository.findByUserId(userId)
      .orElseThrow(() -> new RefreshTokenNotFoundException());

    return refreshToken;
  }
}
