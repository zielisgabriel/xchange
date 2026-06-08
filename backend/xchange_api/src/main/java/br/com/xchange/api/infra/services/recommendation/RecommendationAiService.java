package br.com.xchange.api.infra.services.recommendation;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.xchange.api.infra.services.recommendation.dto.RecommendationAiRequest;
import br.com.xchange.api.infra.services.recommendation.dto.RecommendationAiResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecommendationAiService {
  private final RestTemplate restTemplate;

  public RecommendationAiResponse getFavoritesRecommendations(List<String> ids) {
    RecommendationAiRequest request = new RecommendationAiRequest(ids);
    RecommendationAiResponse response = this.restTemplate.postForObject(
        "http://127.0.0.1:8000/recommend/favorites",
        request,
        RecommendationAiResponse.class);
    
    return response;
  }
}
