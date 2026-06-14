package br.com.xchange.api.domain.ports.services;

import java.util.List;
import java.util.Set;

import br.com.xchange.api.domain.entities.CoinPrediction;

public interface RecommendationCoinServicePort {
  Set<CoinPrediction> getCoinPredictionBySymbols(List<String> symbols);
}
