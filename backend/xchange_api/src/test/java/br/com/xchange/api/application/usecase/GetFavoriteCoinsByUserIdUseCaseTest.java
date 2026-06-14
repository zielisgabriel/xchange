package br.com.xchange.api.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
class GetFavoriteCoinsByUserIdUseCaseTest {

  @Mock
  private ProfileRepositoryPort repositoryPort;

  @InjectMocks
  private GetFavoriteCoinsByUserIdUseCase useCase;

  private static final UUID USER_ID = UUID.randomUUID();

  @Test
  @DisplayName("Deve retornar as moedas favoritas do perfil")
  void shouldReturnProfileFavoriteCoins() {
    FavoriteCoin coin = new FavoriteCoin();
    coin.setCoinId("bitcoin");

    Profile profile = new Profile();
    profile.setFavoriteCoins(Set.of(coin));
    when(repositoryPort.findById(USER_ID)).thenReturn(Optional.of(profile));

    Set<FavoriteCoin> result = useCase.execute(USER_ID);

    assertEquals(Set.of(coin), result);
  }

  @Test
  @DisplayName("Deve lançar UserNotFoundException quando o perfil não existe")
  void shouldThrowWhenProfileNotFound() {
    when(repositoryPort.findById(USER_ID)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> useCase.execute(USER_ID));
  }
}
