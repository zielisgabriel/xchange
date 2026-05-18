package br.com.xchange.api.application.usecase;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.xchange.api.domain.entities.RefreshToken;
import br.com.xchange.api.domain.ports.repositories.RefreshTokenRepositoryPort;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetRefreshTokenUseCase {
  private final RefreshTokenRepositoryPort repository;

  public RefreshToken execute(UUID userId) {
    RefreshToken refreshToken = this.repository.findByUserId(userId)
      .orElseThrow(() -> new RuntimeException("Refresh token não encontrado!"));

    return refreshToken;
  }
}
