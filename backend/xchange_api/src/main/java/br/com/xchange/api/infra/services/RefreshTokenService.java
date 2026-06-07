package br.com.xchange.api.infra.services;

import org.springframework.stereotype.Service;

import br.com.xchange.api.application.usecase.CreateRefreshTokenUseCase;
import br.com.xchange.api.application.usecase.GetRefreshTokenByUserIdUseCase;
import br.com.xchange.api.domain.entities.RefreshToken;
import br.com.xchange.api.domain.exceptions.RefreshTokenAlreadyExistsException;
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
}
