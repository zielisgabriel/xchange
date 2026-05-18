package br.com.xchange.api.infra.controllers;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.xchange.api.application.dto.request.RefreshTokenRequestDto;
import br.com.xchange.api.application.dto.request.RegisterUserRequestDto;
import br.com.xchange.api.application.dto.response.LoginResponseDto;
import br.com.xchange.api.application.dto.response.ProfileResponseDto;
import br.com.xchange.api.application.usecase.RegisterUserUseCase;
import br.com.xchange.api.domain.entities.RefreshToken;
import br.com.xchange.api.domain.exceptions.RefreshTokenNotFoundException;
import br.com.xchange.api.domain.ports.repositories.RefreshTokenRepositoryPort;
import br.com.xchange.api.domain.ports.services.AccessTokenServicePort;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  private final RegisterUserUseCase registerUserUseCase;
  private final RefreshTokenRepositoryPort refreshTokenRepository;
  private final AccessTokenServicePort accessTokenService;

  @PostMapping("/register")
  @ResponseStatus(code = HttpStatus.CREATED)
  public ProfileResponseDto register(@Validated @RequestBody RegisterUserRequestDto requestDto) {
    return ProfileResponseDto.fromDomain(this.registerUserUseCase.execute(requestDto));
  }

  @PostMapping("/refresh")
  public LoginResponseDto refresh(@Validated @RequestBody RefreshTokenRequestDto requestDto) {
    UUID id = UUID.fromString(requestDto.refreshToken());

    RefreshToken refreshToken = this.refreshTokenRepository.findById(id)
      .orElseThrow(RefreshTokenNotFoundException::new);

    String accessToken = this.accessTokenService.generateFromUserId(refreshToken.getUserId());

    return new LoginResponseDto(accessToken, requestDto.refreshToken());
  }
}
