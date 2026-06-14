package br.com.xchange.api.application.usecase;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.xchange.api.application.dto.request.OnboardingRequestDto;
import br.com.xchange.api.domain.entities.AuthUser;
import br.com.xchange.api.domain.entities.FavoriteCoin;
import br.com.xchange.api.domain.entities.Profile;
import br.com.xchange.api.domain.exceptions.UserNotFoundException;
import br.com.xchange.api.domain.ports.repositories.ProfileRepositoryPort;

@ExtendWith(MockitoExtension.class)
class FinishOnboardingUseCaseTest {

  @Mock
  private ProfileRepositoryPort profileRepositoryPort;

  @InjectMocks
  private FinishOnboardingUseCase useCase;

  @Captor
  private ArgumentCaptor<Profile> profileCaptor;

  private static final UUID USER_ID = UUID.randomUUID();

  private FavoriteCoin favoriteCoin(String id, String symbol) {
    FavoriteCoin coin = new FavoriteCoin();
    coin.setCoinId(id);
    coin.setSymbol(symbol);
    return coin;
  }

  @Test
  @DisplayName("Deve substituir as moedas favoritas e finalizar o onboarding")
  void shouldReplaceFavoriteCoinsAndFinishOnboarding() {
    AuthUser authUser = new AuthUser();
    Profile profile = new Profile();
    profile.setAuthUser(authUser);
    profile.setFavoriteCoins(new HashSet<>(Set.of(favoriteCoin("bitcoin", "BTC"))));

    Set<FavoriteCoin> newFavorites = Set.of(favoriteCoin("ethereum", "ETH"));
    OnboardingRequestDto request = new OnboardingRequestDto(newFavorites);

    when(profileRepositoryPort.findById(USER_ID)).thenReturn(Optional.of(profile));

    useCase.execute(USER_ID, request);

    verify(profileRepositoryPort).save(profileCaptor.capture());
    Profile saved = profileCaptor.getValue();

    assertTrue(saved.getAuthUser().isOnboardingFinished());
    assertTrue(saved.getFavoriteCoins().containsAll(newFavorites));
  }

  @Test
  @DisplayName("Deve lançar UserNotFoundException quando o perfil não existe")
  void shouldThrowWhenProfileNotFound() {
    OnboardingRequestDto request = new OnboardingRequestDto(Set.of(favoriteCoin("ethereum", "ETH")));
    when(profileRepositoryPort.findById(USER_ID)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> useCase.execute(USER_ID, request));
  }
}
