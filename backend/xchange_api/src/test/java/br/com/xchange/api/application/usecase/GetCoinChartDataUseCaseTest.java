package br.com.xchange.api.application.usecase;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.xchange.api.domain.entities.CoinChartData;
import br.com.xchange.api.domain.ports.services.CoinServicePort;

@ExtendWith(MockitoExtension.class)
class GetCoinChartDataUseCaseTest {

  @Mock
  private CoinServicePort coinServicePort;

  @InjectMocks
  private GetCoinChartDataUseCase useCase;

  @Test
  @DisplayName("Deve delegar a busca do gráfico ao serviço de moedas")
  void shouldDelegateChartLookupToService() {
    CoinChartData chartData = new CoinChartData();
    when(coinServicePort.getChartDataById("bitcoin")).thenReturn(chartData);

    CoinChartData result = useCase.execute("bitcoin");

    assertSame(chartData, result);
    verify(coinServicePort).getChartDataById("bitcoin");
  }
}
