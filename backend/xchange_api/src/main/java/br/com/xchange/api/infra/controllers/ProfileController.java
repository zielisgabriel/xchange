package br.com.xchange.api.infra.controllers;

import java.security.Principal;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.xchange.api.application.dto.request.OnboardingRequestDto;
import br.com.xchange.api.application.dto.response.ProfileResponseDto;
import br.com.xchange.api.application.dto.response.ProfileSimpleResponseDto;
import br.com.xchange.api.application.usecase.GetProfileUseCase;
import br.com.xchange.api.application.utils.PrincipalUtils;
import br.com.xchange.api.domain.entities.FavoriteCoin;
import br.com.xchange.api.application.usecase.FinishOnboardingUseCase;
import br.com.xchange.api.application.usecase.GetFavoriteCoinsByUserId;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {
  private final GetProfileUseCase getProfileUseCase;
  private final FinishOnboardingUseCase finishOnboardingUseCase;
  private final GetFavoriteCoinsByUserId getFavoriteCoinsByUserId;

  @GetMapping("/details")
  public ProfileResponseDto getDetails(Principal principal) {
    return ProfileResponseDto.fromDomain(this.getProfileUseCase.execute(PrincipalUtils.recoverUserId(principal)));
  }

  @GetMapping("/simple")
  public ProfileSimpleResponseDto getSimple(Principal principal) {
    return ProfileSimpleResponseDto.fromDomain(this.getProfileUseCase.execute(PrincipalUtils.recoverUserId(principal)));
  }

  @PostMapping("/onboarding")
  @ResponseStatus(value = HttpStatus.CREATED)
  public void finishOnboarding(
    Principal principal,
    @Valid @RequestBody OnboardingRequestDto onboardingRequestDto
  ) {
    this.finishOnboardingUseCase
      .execute(PrincipalUtils.recoverUserId(principal), onboardingRequestDto);
  }

  @GetMapping("/favorite-coins")
  @ResponseStatus(value = HttpStatus.OK)
  public Set<FavoriteCoin> getFavoriteCoins(Principal principal) {
    UUID userId = PrincipalUtils.recoverUserId(principal);

    return this.getFavoriteCoinsByUserId.execute(userId);
  }
}
