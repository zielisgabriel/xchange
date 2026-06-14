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

import br.com.xchange.api.domain.entities.CoinWithMarketData;
import br.com.xchange.api.domain.ports.services.CoinServicePort;

@ExtendWith(MockitoExtension.class)
class GetCoinsWithMarketDataUseCaseTest {

  @Mock
  private CoinServicePort coinServicePort;

  @InjectMocks
  private GetCoinsWithMarketDataUseCase useCase;

  @Test
  @DisplayName("Deve delegar a lista de moedas com dados de mercado ao serviço")
  void shouldDelegateMarketDataListToService() {
    CoinWithMarketData coin = new CoinWithMarketData();
    coin.setId("bitcoin");
    when(coinServicePort.getCoinsWithMarketData()).thenReturn(List.of(coin));

    List<CoinWithMarketData> result = useCase.execute();

    assertEquals(List.of(coin), result);
    verify(coinServicePort).getCoinsWithMarketData();
  }
}
