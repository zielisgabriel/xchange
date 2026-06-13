package br.com.xchange.api.infra.adapters.services;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.xchange.api.domain.entities.CoinPrediction;
import br.com.xchange.api.domain.ports.services.RecommendationCoinServicePort;
import br.com.xchange.api.infra.services.crypto_recommendation_ai.CryptoRecommendationAIService;
import br.com.xchange.api.infra.services.crypto_recommendation_ai.dto.CoinPredictionResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CryptoRecommendationAIServiceAdapter implements RecommendationCoinServicePort {
  private final CryptoRecommendationAIService service;

  @Override
  public Set<CoinPrediction> getCoinPredictionBySymbols(List<String> symbols) {
    CoinPredictionResponse response = this.service.getCoinPredictionBySymbols(symbols);

    if (response == null || response.data() == null) {
      return Collections.emptySet();
    }

    return response.data().stream().map((prediction) -> {
      CoinPrediction coinPrediction = new CoinPrediction();
      coinPrediction.setId(prediction.id());
      coinPrediction.setName(prediction.name());
      coinPrediction.setPrediction(prediction.prediction());
      coinPrediction.setSymbol(prediction.symbol());

      return coinPrediction;
    }).collect(Collectors.toSet());
  }
}
