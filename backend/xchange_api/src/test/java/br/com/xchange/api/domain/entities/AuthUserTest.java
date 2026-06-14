package br.com.xchange.api.domain.entities;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthUserTest {

  @Test
  @DisplayName("Onboarding deve começar como não finalizado")
  void onboardingShouldStartUnfinished() {
    AuthUser authUser = new AuthUser();

    assertFalse(authUser.isOnboardingFinished());
  }

  @Test
  @DisplayName("finishOnboarding deve marcar o onboarding como finalizado")
  void finishOnboardingShouldMarkAsFinished() {
    AuthUser authUser = new AuthUser();

    authUser.finishOnboarding();

    assertTrue(authUser.isOnboardingFinished());
  }
}
