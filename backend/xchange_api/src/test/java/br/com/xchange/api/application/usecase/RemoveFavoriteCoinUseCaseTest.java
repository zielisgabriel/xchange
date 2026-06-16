package br.com.xchange.api.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
import br.com.xchange.api.domain.exceptions.UserNotFoundException;
import br.com.xchange.api.domain.ports.repositories.ProfileRepositoryPort;

@ExtendWith(MockitoExtension.class)
class RemoveFavoriteCoinUseCaseTest {

  @Mock
  private ProfileRepositoryPort profileRepositoryPort;

  @InjectMocks
  private RemoveFavoriteCoinUseCase useCase;

  private static final UUID USER_ID = UUID.randomUUID();

  private FavoriteCoin coin(String coinId) {
    FavoriteCoin favoriteCoin = new FavoriteCoin();
    favoriteCoin.setCoinId(coinId);
    favoriteCoin.setName(coinId);
    favoriteCoin.setSymbol(coinId.toUpperCase());
    return favoriteCoin;
  }

  @Test
  @DisplayName("Deve remover a moeda e retornar a lista atualizada")
  void shouldRemoveCoin() {
    Profile profile = new Profile();
    profile.setId(USER_ID);
    profile.getFavoriteCoins().add(coin("bitcoin"));
    profile.getFavoriteCoins().add(coin("ethereum"));
    when(profileRepositoryPort.findById(USER_ID)).thenReturn(Optional.of(profile));
    when(profileRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

    Set<FavoriteCoin> result = useCase.execute(USER_ID, "bitcoin");

    assertEquals(1, result.size());
    assertFalse(result.stream().anyMatch(c -> c.getCoinId().equals("bitcoin")));
    verify(profileRepositoryPort).save(profile);
  }

  @Test
  @DisplayName("Deve lançar UserNotFoundException quando o perfil não existe")
  void shouldThrowWhenProfileNotFound() {
    when(profileRepositoryPort.findById(USER_ID)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> useCase.execute(USER_ID, "bitcoin"));
  }
}
