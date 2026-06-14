package br.com.xchange.api.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.xchange.api.application.dto.response.GlobalCoinMetricsResponse;
import br.com.xchange.api.domain.entities.GlobalCoinMetricsData;
import br.com.xchange.api.domain.ports.services.CoinServicePort;

@ExtendWith(MockitoExtension.class)
class GetGlobalCoinMetricsUseCaseTest {

  @Mock
  private CoinServicePort coinServicePort;

  @InjectMocks
  private GetGlobalCoinMetricsUseCase useCase;

  @Test
  @DisplayName("Deve mapear a entidade de domínio para o DTO de resposta")
  void shouldMapDomainEntityToResponseDto() {
    GlobalCoinMetricsData metrics = new GlobalCoinMetricsData();
    metrics.setTotalMarketCap(new BigDecimal("1000"));
    metrics.setTotalVolume(new BigDecimal("200"));
    metrics.setMarketCapChangePercentage24hUsd(new BigDecimal("1.5"));
    metrics.setVolumeChangePercentage24hUsd(new BigDecimal("-0.5"));

    when(coinServicePort.getGlobalCoinMetrics()).thenReturn(metrics);

    GlobalCoinMetricsResponse response = useCase.execute();

    assertEquals(new BigDecimal("1000"), response.data().totalMarketCap());
    assertEquals(new BigDecimal("200"), response.data().totalVolume());
    assertEquals(new BigDecimal("1.5"), response.data().marketCapChangePercentage24hUsd());
    assertEquals(new BigDecimal("-0.5"), response.data().volumeChangePercentage24hUsd());
  }
}
