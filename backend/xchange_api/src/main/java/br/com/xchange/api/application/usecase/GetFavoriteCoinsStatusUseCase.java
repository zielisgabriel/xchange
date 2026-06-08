package br.com.xchange.api.application.usecase;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.xchange.api.application.dto.response.FavoriteCoinStatusResponse;
import br.com.xchange.api.domain.entities.FavoriteCoin;
import br.com.xchange.api.domain.entities.Profile;
import br.com.xchange.api.domain.ports.repositories.ProfileRepositoryPort;
import br.com.xchange.api.infra.services.coingecko.CoinsGeckoService;
import br.com.xchange.api.infra.services.coingecko.dto.CoinInListWithMarketData;
import br.com.xchange.api.infra.services.recommendation.RecommendationAiService;
import br.com.xchange.api.infra.services.recommendation.dto.RecommendationAiResponse;
import br.com.xchange.api.infra.services.recommendation.dto.RecommendationAiResponse.CoinPrediction;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetFavoriteCoinsStatusUseCase {

  private final StringRedisTemplate redisTemplate;
  private final RecommendationAiService recommendationAiService;
  private final ProfileRepositoryPort profileRepositoryPort;
  private final CoinsGeckoService coinsGeckoService;
  private final ObjectMapper objectMapper = new ObjectMapper();

  private static final String REDIS_PREFIX = "fav_coin:";
  private static final Duration TTL = Duration.ofMinutes(10);

  public List<FavoriteCoinStatusResponse> execute(UUID userId) {
    Profile profile = profileRepositoryPort.findById(userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil não encontrado"));

    List<FavoriteCoinStatusResponse> result = new ArrayList<>();
    List<String> missingIds = new ArrayList<>();
    List<FavoriteCoin> missingCoinsInfo = new ArrayList<>();

    for (FavoriteCoin coin : profile.getFavoriteCoins()) {
      String key = REDIS_PREFIX + coin.getCoinId();
      String cachedValue = redisTemplate.opsForValue().get(key);

      if (cachedValue != null) {
        try {
          FavoriteCoinStatusResponse cachedStatus = objectMapper.readValue(cachedValue, FavoriteCoinStatusResponse.class);
          result.add(cachedStatus);
        } catch (JsonProcessingException e) {
          missingIds.add(coin.getCoinId());
          missingCoinsInfo.add(coin);
        }
      } else {
        missingIds.add(coin.getCoinId());
        missingCoinsInfo.add(coin);
      }
    }

    if (!missingIds.isEmpty()) {
      try {
        RecommendationAiResponse aiResponse = recommendationAiService.getFavoritesRecommendations(missingIds);
        List<CoinInListWithMarketData> geckoCoins = coinsGeckoService.getCoinsByIds(missingIds);

        for (FavoriteCoin favCoin : missingCoinsInfo) {
          FavoriteCoinStatusResponse responseItem = new FavoriteCoinStatusResponse();
          responseItem.setId(favCoin.getCoinId());
          responseItem.setName(favCoin.getName());
          responseItem.setSymbol(favCoin.getSymbol());
          
          String predictionValue = "no_data";
          if (aiResponse != null && aiResponse.getData() != null) {
            predictionValue = aiResponse.getData().stream()
                .filter(p -> p.getId().equals(favCoin.getCoinId()))
                .map(CoinPrediction::getPrediction)
                .findFirst()
                .orElse("no_data");
          }
          responseItem.setStatus(mapPredictionToStatus(predictionValue));

          geckoCoins.stream()
              .filter(c -> c.id().equals(favCoin.getCoinId()))
              .findFirst()
              .ifPresent(geckoCoin -> responseItem.setImageUrl(geckoCoin.image()));

          result.add(responseItem);

          try {
            String jsonValue = objectMapper.writeValueAsString(responseItem);
            redisTemplate.opsForValue().set(REDIS_PREFIX + favCoin.getCoinId(), jsonValue, TTL);
          } catch (JsonProcessingException e) {
            
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    return result;
  }

  private String mapPredictionToStatus(String prediction) {
    if (prediction == null) return "no_data";
    switch (prediction.toLowerCase()) {
      case "alta": return "up";
      case "baixa": return "down";
      case "lateral": return "stable";
      case "no_data": return "no_data";
      default: return "no_data";
    }
  }
}

