package br.com.xchange.api.infra.adapters.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.xchange.api.domain.entities.CoinPrediction;
import br.com.xchange.api.infra.services.crypto_recommendation_ai.CryptoRecommendationAIService;
import br.com.xchange.api.infra.services.crypto_recommendation_ai.dto.CoinPredictionResponse;
import br.com.xchange.api.infra.services.crypto_recommendation_ai.dto.CoinPredictionResponse.CoinPredictionData;

@ExtendWith(MockitoExtension.class)
class CryptoRecommendationAIServiceAdapterTest {

  @Mock
  private CryptoRecommendationAIService service;

  @InjectMocks
  private CryptoRecommendationAIServiceAdapter adapter;

  @Test
  @DisplayName("Deve retornar conjunto vazio quando a resposta é nula")
  void shouldReturnEmptyWhenResponseNull() {
    when(service.getCoinPredictionBySymbols(anyList())).thenReturn(null);

    assertTrue(adapter.getCoinPredictionBySymbols(List.of("BTC")).isEmpty());
  }

  @Test
  @DisplayName("Deve retornar conjunto vazio quando os dados da resposta são nulos")
  void shouldReturnEmptyWhenDataNull() {
    when(service.getCoinPredictionBySymbols(anyList()))
        .thenReturn(new CoinPredictionResponse(0, null));

    assertTrue(adapter.getCoinPredictionBySymbols(List.of("BTC")).isEmpty());
  }

  @Test
  @DisplayName("Deve mapear as predições retornadas pelo serviço de IA")
  void shouldMapPredictions() {
    CoinPredictionResponse response = new CoinPredictionResponse(
        1, List.of(new CoinPredictionData("bitcoin", "BTC", "Bitcoin", "UP")));
    when(service.getCoinPredictionBySymbols(anyList())).thenReturn(response);

    Set<CoinPrediction> result = adapter.getCoinPredictionBySymbols(List.of("BTC"));

    assertEquals(1, result.size());
    CoinPrediction prediction = result.iterator().next();
    assertEquals("bitcoin", prediction.getId());
    assertEquals("BTC", prediction.getSymbol());
    assertEquals("Bitcoin", prediction.getName());
    assertEquals("UP", prediction.getPrediction());
  }
}
