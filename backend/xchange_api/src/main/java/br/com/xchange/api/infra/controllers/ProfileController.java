package br.com.xchange.api.infra.controllers;

import java.security.Principal;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.xchange.api.application.dto.request.CreateProfileRequestDto;
import br.com.xchange.api.application.dto.response.ProfileResponseDto;
import br.com.xchange.api.application.usecase.CreateProfileUseCase;
import br.com.xchange.api.application.usecase.GetProfileUseCase;
import br.com.xchange.api.domain.exceptions.ForbiddenChangeAnotherUserInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {
  private final CreateProfileUseCase createProfileUseCase;
  private final GetProfileUseCase getProfileUseCase;

  @PostMapping("/create")
  public ProfileResponseDto createProfile(Principal principal, @RequestBody CreateProfileRequestDto requestDto) {
    if (!this.recoverUserId(principal).equals(requestDto.userId())) throw new ForbiddenChangeAnotherUserInfoException();

    return ProfileResponseDto.fromDomain(this.createProfileUseCase.execute(requestDto));
  }

  @GetMapping("/me")
  public ProfileResponseDto getMe(Principal principal) {
    return ProfileResponseDto.fromDomain(this.getProfileUseCase.execute(recoverUserId(principal)));
  }

  private UUID recoverUserId(Principal principal) {
    return UUID.fromString(principal.getName());
  }
}
