package br.com.xchange.api.infra.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.xchange.api.application.usecase.CreateRefreshTokenUseCase;
import br.com.xchange.api.application.usecase.GetRefreshTokenByUserIdUseCase;
import br.com.xchange.api.domain.entities.RefreshToken;
import br.com.xchange.api.domain.exceptions.RefreshTokenAlreadyExistsException;
import br.com.xchange.api.domain.exceptions.RefreshTokenNotFoundException;
import br.com.xchange.api.domain.exceptions.UserNotFoundException;
import br.com.xchange.api.domain.ports.repositories.AuthUserRepositoryPort;
import br.com.xchange.api.domain.ports.repositories.RefreshTokenRepositoryPort;
import br.com.xchange.api.domain.ports.services.RefreshTokenServicePort;
import br.com.xchange.api.infra.entities.XchangeUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenService implements RefreshTokenServicePort {
  private final CreateRefreshTokenUseCase createRefreshTokenUseCase;
  private final GetRefreshTokenByUserIdUseCase getRefreshTokenByUserIdUseCase;
  private final AuthUserRepositoryPort authUserRepositoryPort;
  private final RefreshTokenRepositoryPort refreshTokenRepositoryPort;

  public String generate(Object principal) {
    XchangeUserDetails userDetails = (XchangeUserDetails) principal;

    try {
      RefreshToken refreshToken = this.createRefreshTokenUseCase.execute(userDetails.getId());
      
      return refreshToken.getId().toString();
    } catch (RefreshTokenAlreadyExistsException exception) {
      log.error(exception.getMessage() + "ID: {}", userDetails.getId().toString());
      RefreshToken refreshToken = this.getRefreshTokenByUserIdUseCase.execute(userDetails.getId());

      return refreshToken.getId().toString();
    }
  }

  @Override
  public RefreshToken validate(UUID refreshTokenId) {
    RefreshToken refreshToken = this.refreshTokenRepositoryPort.findById(refreshTokenId)
      .orElseThrow(RefreshTokenNotFoundException::new);

    UUID userId = refreshToken.getUserId();

    this.authUserRepositoryPort.findById(userId)
      .orElseThrow(UserNotFoundException::new);

    return refreshToken;
  }
}
