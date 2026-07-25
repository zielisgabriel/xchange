package br.com.xchange.api.infra.services.crypto_recommendation_ai;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.xchange.api.infra.services.crypto_recommendation_ai.dto.CoinPredictionResponse;
import lombok.extern.slf4j.Slf4j;

import br.com.xchange.api.application.dto.request.CoinPredictionRequest;

@Slf4j
@Service
public class CryptoRecommendationAIService {
  private final RestTemplate restTemplate;
  private final String predictionUrl;

  public CryptoRecommendationAIService(
    RestTemplate restTemplate,
    @Value("${crypto-recommendation-ai.prediction-url}") String predictionUrl
  ) {
    this.restTemplate = restTemplate;
    this.predictionUrl = predictionUrl;
  }

  public CoinPredictionResponse getCoinPredictionBySymbols(List<String> symbols) {
    try {
      HttpEntity<CoinPredictionRequest> requestHttpEntity = new HttpEntity<CoinPredictionRequest>(new CoinPredictionRequest(symbols));

      CoinPredictionResponse coinPredictionResponse = this.restTemplate
        .postForObject(this.predictionUrl, requestHttpEntity, CoinPredictionResponse.class);

      return coinPredictionResponse;
    } catch (Exception e) {
      log.error("Falha ao obter predições do serviço de IA: {}", e.getMessage());
      return null;
    }
  }
}
