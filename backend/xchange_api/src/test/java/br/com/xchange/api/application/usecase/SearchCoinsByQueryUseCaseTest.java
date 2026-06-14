package br.com.xchange.api.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.xchange.api.domain.entities.Coin;
import br.com.xchange.api.domain.ports.services.CoinServicePort;

@ExtendWith(MockitoExtension.class)
class SearchCoinsByQueryUseCaseTest {

  @Mock
  private CoinServicePort coinServicePort;

  @InjectMocks
  private SearchCoinsByQueryUseCase useCase;

  @Test
  @DisplayName("Deve delegar a busca por consulta ao serviço de moedas")
  void shouldDelegateSearchToService() {
    Coin coin = new Coin();
    coin.setId("bitcoin");
    when(coinServicePort.getCoinsByQuery("btc")).thenReturn(List.of(coin));

    List<Coin> result = useCase.execute("btc");

    assertEquals(List.of(coin), result);
    verify(coinServicePort).getCoinsByQuery("btc");
  }
}
