package br.com.xchange.api.domain.ports.services;

import java.util.List;

import br.com.xchange.api.application.dto.response.GlobalCoinMetricsResponse;
import br.com.xchange.api.domain.entities.Coin;
import br.com.xchange.api.domain.entities.CoinChartData;
import br.com.xchange.api.domain.entities.CoinDetailData;
import br.com.xchange.api.domain.entities.CoinWithMarketData;

public interface CoinServicePort {
  List<Coin> getTrendingCoins();
  List<CoinWithMarketData> getCoinsWithMarketData();
  List<CoinWithMarketData> getTrendingCoinsDetailed();
  GlobalCoinMetricsResponse getGlobalCoinMetrics();
  CoinChartData getChartDataById(String coinId);
  public CoinDetailData getCoinDetailById(String coinId);
}
