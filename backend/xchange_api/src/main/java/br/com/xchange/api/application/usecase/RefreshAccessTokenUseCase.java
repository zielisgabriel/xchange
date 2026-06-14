package br.com.xchange.api.application.usecase;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.xchange.api.application.dto.response.LoginResponseDto;
import br.com.xchange.api.domain.entities.RefreshToken;
import br.com.xchange.api.domain.ports.services.AccessTokenServicePort;
import br.com.xchange.api.domain.ports.services.RefreshTokenServicePort;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshAccessTokenUseCase {
  private final RefreshTokenServicePort refreshTokenServicePort;
  private final AccessTokenServicePort accessTokenServicePort;

  public LoginResponseDto execute(UUID refreshTokenId) {
    RefreshToken refreshToken = this.refreshTokenServicePort.validate(refreshTokenId);
    String accessToken = this.accessTokenServicePort.generateFromUserId(refreshToken.getUserId());

    return new LoginResponseDto(accessToken, refreshToken.getId().toString());
  }
}
