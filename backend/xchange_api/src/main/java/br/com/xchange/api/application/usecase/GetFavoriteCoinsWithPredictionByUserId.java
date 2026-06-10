package br.com.xchange.api.application.usecase;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.xchange.api.domain.entities.CoinPrediction;
import br.com.xchange.api.domain.entities.FavoriteCoin;
import br.com.xchange.api.domain.entities.FavoriteCoinWithPrediction;
import br.com.xchange.api.domain.entities.Profile;
import br.com.xchange.api.domain.exceptions.UserNotFoundException;
import br.com.xchange.api.domain.ports.repositories.ProfileRepositoryPort;
import br.com.xchange.api.domain.ports.services.RecommendationCoinServicePort;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetFavoriteCoinsWithPredictionByUserId {
  private final ProfileRepositoryPort repositoryPort;
  private final RecommendationCoinServicePort recommendationCoinServicePort;
  
  public Set<FavoriteCoinWithPrediction> execute(UUID userId) {
    Profile profile = this.repositoryPort.findById(userId)
      .orElseThrow(() -> new UserNotFoundException());

    Set<FavoriteCoin> profileFavoriteCoins = profile.getFavoriteCoins();

    List<String> symbols = profileFavoriteCoins.stream()
      .map(coin -> coin.getSymbol())
      .toList();

    Set<CoinPrediction> coinPredictions = this.recommendationCoinServicePort.getCoinPredictionBySymbols(symbols);

    return profileFavoriteCoins.stream().map(coin -> {
      FavoriteCoinWithPrediction favoriteCoinWithPrediction = new FavoriteCoinWithPrediction();
      favoriteCoinWithPrediction.setCoinId(coin.getCoinId());
      favoriteCoinWithPrediction.setName(coin.getName());
      favoriteCoinWithPrediction.setSymbol(coin.getSymbol());
      favoriteCoinWithPrediction.setImageUrl(coin.getImageUrl());
      
      String prediction = coinPredictions.stream()
        .filter(p -> p.getSymbol().equals(coin.getSymbol()))
        .findFirst()
        .map(CoinPrediction::getPrediction)
        .orElse(null);
        
      favoriteCoinWithPrediction.setPrediction(prediction);
      
      return favoriteCoinWithPrediction;
    }).collect(Collectors.toSet());
  }
}
