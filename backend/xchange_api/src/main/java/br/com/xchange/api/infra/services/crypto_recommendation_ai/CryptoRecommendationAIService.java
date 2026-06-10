package br.com.xchange.api.infra.services.crypto_recommendation_ai;

import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.xchange.api.infra.services.crypto_recommendation_ai.dto.CoinPredictionResponse;
import lombok.RequiredArgsConstructor;

import br.com.xchange.api.application.dto.request.CoinPredictionRequest;

@Service
@RequiredArgsConstructor
public class CryptoRecommendationAIService {
  private final RestTemplate restTemplate;

  public CoinPredictionResponse getCoinPredictionBySymbols(List<String> symbols) {
    HttpEntity<CoinPredictionRequest> requestHttpEntity = new HttpEntity<CoinPredictionRequest>(new CoinPredictionRequest(symbols));

    CoinPredictionResponse coinPredictionResponse = this.restTemplate
      .postForObject("http://localhost:8000/predict", requestHttpEntity, CoinPredictionResponse.class);

    return coinPredictionResponse;
  }
}
