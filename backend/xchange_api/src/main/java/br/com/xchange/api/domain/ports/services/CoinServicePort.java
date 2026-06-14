package br.com.xchange.api.domain.ports.services;

import java.util.List;

import br.com.xchange.api.domain.entities.Coin;
import br.com.xchange.api.domain.entities.CoinChartData;
import br.com.xchange.api.domain.entities.CoinDetailData;
import br.com.xchange.api.domain.entities.CoinWithMarketData;
import br.com.xchange.api.domain.entities.GlobalCoinMetricsData;

public interface CoinServicePort {
  List<Coin> getTrendingCoins();
  List<CoinWithMarketData> getCoinsWithMarketData();
  List<CoinWithMarketData> getTrendingCoinsDetailed();
  GlobalCoinMetricsData getGlobalCoinMetrics();
  CoinChartData getChartDataById(String coinId);
  CoinDetailData getCoinDetailById(String coinId);
  List<Coin> getCoinsByQuery(String query);
}
