package br.com.xchange.api.infra.controllers;

import java.security.Principal;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.xchange.api.application.dto.response.ProfileResponseDto;
import br.com.xchange.api.application.dto.response.ProfileSimpleResponseDto;
import br.com.xchange.api.application.usecase.GetProfileUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {
  private final GetProfileUseCase getProfileUseCase;

  @GetMapping("/me")
  public ProfileResponseDto getMe(Principal principal) {
    return ProfileResponseDto.fromDomain(this.getProfileUseCase.execute(recoverUserId(principal)));
  }

  @GetMapping("/simple")
  public ProfileSimpleResponseDto getSimple(Principal principal) {
    return ProfileSimpleResponseDto.fromDomain(this.getProfileUseCase.execute(recoverUserId(principal)));
  }

  private UUID recoverUserId(Principal principal) {
    return UUID.fromString(principal.getName());
  }
}
