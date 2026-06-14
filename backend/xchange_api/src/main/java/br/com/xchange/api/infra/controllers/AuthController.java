package br.com.xchange.api.infra.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.xchange.api.application.dto.request.RefreshTokenRequestDto;
import br.com.xchange.api.application.dto.request.RegisterUserRequestDto;
import br.com.xchange.api.application.dto.response.LoginResponseDto;
import br.com.xchange.api.application.dto.response.ProfileResponseDto;
import br.com.xchange.api.application.usecase.RefreshAccessTokenUseCase;
import br.com.xchange.api.application.usecase.RegisterUserUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  private final RegisterUserUseCase registerUserUseCase;
  private final RefreshAccessTokenUseCase refreshAccessTokenUseCase;

  @PostMapping("/register")
  @ResponseStatus(code = HttpStatus.CREATED)
  public ProfileResponseDto register(
    @Valid @RequestBody RegisterUserRequestDto requestDto
  ) {
    return ProfileResponseDto.fromDomain(this.registerUserUseCase.execute(requestDto));
  }

  @PostMapping("/refresh")
  public LoginResponseDto refresh(
    @Valid @RequestBody RefreshTokenRequestDto requestDto
  ) {
    return this.refreshAccessTokenUseCase.execute(requestDto.refreshToken());
  }
}
