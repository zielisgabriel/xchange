package br.com.xchange.api.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.xchange.api.domain.entities.FavoriteCoin;
import br.com.xchange.api.domain.entities.Profile;
import br.com.xchange.api.domain.exceptions.FavoriteCoinsLimitExceededException;
import br.com.xchange.api.domain.exceptions.UserNotFoundException;
import br.com.xchange.api.domain.ports.repositories.ProfileRepositoryPort;

@ExtendWith(MockitoExtension.class)
class AddFavoriteCoinUseCaseTest {

  @Mock
  private ProfileRepositoryPort profileRepositoryPort;

  @InjectMocks
  private AddFavoriteCoinUseCase useCase;

  private static final UUID USER_ID = UUID.randomUUID();

  private FavoriteCoin coin(String coinId) {
    FavoriteCoin favoriteCoin = new FavoriteCoin();
    favoriteCoin.setCoinId(coinId);
    favoriteCoin.setName(coinId);
    favoriteCoin.setSymbol(coinId.toUpperCase());
    return favoriteCoin;
  }

  private Profile profileWith(int favorites) {
    Profile profile = new Profile();
    profile.setId(USER_ID);
    for (int i = 0; i < favorites; i++) {
      profile.getFavoriteCoins().add(coin("coin-" + i));
    }
    return profile;
  }

  @Test
  @DisplayName("Deve adicionar a moeda e retornar a lista atualizada")
  void shouldAddCoin() {
    Profile profile = profileWith(2);
    when(profileRepositoryPort.findById(USER_ID)).thenReturn(Optional.of(profile));
    when(profileRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

    Set<FavoriteCoin> result = useCase.execute(USER_ID, coin("bitcoin"));

    assertEquals(3, result.size());
    assertTrue(result.stream().anyMatch(c -> c.getCoinId().equals("bitcoin")));
    verify(profileRepositoryPort).save(profile);
  }

  @Test
  @DisplayName("Deve lançar exceção e não salvar ao atingir o limite de 5")
  void shouldThrowAndNotSaveWhenLimitReached() {
    Profile profile = profileWith(Profile.MAX_FAVORITE_COINS);
    when(profileRepositoryPort.findById(USER_ID)).thenReturn(Optional.of(profile));

    assertThrows(FavoriteCoinsLimitExceededException.class,
        () -> useCase.execute(USER_ID, coin("bitcoin")));

    verify(profileRepositoryPort, never()).save(any());
  }

  @Test
  @DisplayName("Adicionar uma favorita já existente é idempotente")
  void shouldBeIdempotentForDuplicate() {
    Profile profile = new Profile();
    profile.setId(USER_ID);
    profile.getFavoriteCoins().add(coin("bitcoin"));
    when(profileRepositoryPort.findById(USER_ID)).thenReturn(Optional.of(profile));
    when(profileRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

    Set<FavoriteCoin> result = useCase.execute(USER_ID, coin("bitcoin"));

    assertEquals(1, result.size());
  }

  @Test
  @DisplayName("Deve lançar UserNotFoundException quando o perfil não existe")
  void shouldThrowWhenProfileNotFound() {
    when(profileRepositoryPort.findById(USER_ID)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> useCase.execute(USER_ID, coin("bitcoin")));
  }
}
