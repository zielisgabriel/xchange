package br.com.xchange.api.infra.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.xchange.api.application.dto.request.RegisterUserRequestDto;
import br.com.xchange.api.application.usecase.RegisterUserUseCase;
import br.com.xchange.api.domain.entities.AuthUser;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  private final RegisterUserUseCase registerUserUseCase;

  @PostMapping("/register")
  @ResponseStatus(code = HttpStatus.CREATED)
  public AuthUser register(@Validated @RequestBody RegisterUserRequestDto requestDto) {
    return this.registerUserUseCase.execute(requestDto);
  }
}
