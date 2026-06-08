package br.com.xchange.api.application.usecase;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.xchange.api.application.dto.request.OnboardingRequestDto;
import br.com.xchange.api.domain.entities.Profile;
import br.com.xchange.api.domain.exceptions.UserNotFoundException;
import br.com.xchange.api.domain.ports.repositories.ProfileRepositoryPort;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FinishOnboardingUseCase {
  private final ProfileRepositoryPort profileRepositoryPort;

  public void execute(UUID userId, OnboardingRequestDto onboardingRequestDto) {
    Profile profile = this.profileRepositoryPort.findById(userId)
      .orElseThrow(() -> new UserNotFoundException());

    profile.getFavoriteCoins().clear();
    profile.setFavoriteCoins(onboardingRequestDto.favoriteCoins());
    profile.getAuthUser().finishOnboarding();

    this.profileRepositoryPort.save(profile);
  }
}
