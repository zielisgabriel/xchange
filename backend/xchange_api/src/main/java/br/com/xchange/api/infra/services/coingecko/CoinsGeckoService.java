package br.com.xchange.api.infra.services.coingecko;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.xchange.api.infra.providers.CoinGeckoRestProvider;
import br.com.xchange.api.infra.services.coingecko.dto.CoinDetail;
import br.com.xchange.api.infra.services.coingecko.dto.CoinHistoricalChartData;
import br.com.xchange.api.infra.services.coingecko.dto.CoinInListWithMarketData;
import br.com.xchange.api.infra.services.coingecko.dto.CoinsByQuery;
import br.com.xchange.api.infra.services.coingecko.dto.GlobalCoinMetrics;
import br.com.xchange.api.infra.services.coingecko.dto.TrendingCoinsCoinsGecko;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CoinsGeckoService {
  private final CoinGeckoRestProvider coinGeckoRestProvider;

  public List<CoinInListWithMarketData> getCoinsListWithMarketData() {
    CoinInListWithMarketData[] response = this.coinGeckoRestProvider
      .getForObject("/coins/markets?vs_currency=usd&per_page=15", CoinInListWithMarketData[].class);

    return response != null ? List.of(response) : List.of();
  }

  public List<CoinInListWithMarketData> getCoinsByIds(List<String> ids) {
    if (ids == null || ids.isEmpty()) {
      return List.of();
    }
    String idsParam = String.join(",", ids);
    CoinInListWithMarketData[] response = this.coinGeckoRestProvider
      .getForObject("/coins/markets?vs_currency=usd&ids=" + idsParam, CoinInListWithMarketData[].class);

    return response != null ? List.of(response) : List.of();
  }

  public TrendingCoinsCoinsGecko getTrendingCoins() {
    TrendingCoinsCoinsGecko response = this.coinGeckoRestProvider
      .getForObject("/search/trending", TrendingCoinsCoinsGecko.class);

    return response;
  };

  public GlobalCoinMetrics getGlobalCoinMetrics() {
    GlobalCoinMetrics response = this.coinGeckoRestProvider
      .getForObject("/global", GlobalCoinMetrics.class);

    return response;
  }

  public CoinHistoricalChartData getChartDataById(String coinId) {
    CoinHistoricalChartData response = this.coinGeckoRestProvider
      .getForObject("/coins/" + coinId + "/market_chart?vs_currency=usd&days=1&interval=hourly&precision=full", CoinHistoricalChartData.class);

    return response;
  }

  public CoinDetail getCoinDetailById(String coinId) {
    CoinDetail response = this.coinGeckoRestProvider
      .getForObject(
        "/coins/" + coinId +
        "?market_data=true&sparkline=true&localization=false&tickers=false&community_data=false&developer_data=false&include_categories_details=false&dex_pair_format=symbol",
        CoinDetail.class
      );

    return response;
  }

  public CoinsByQuery getCoinsByQuery(String query) {
    CoinsByQuery response = this.coinGeckoRestProvider
      .getForObject(
        "/search?query=" + query,
        CoinsByQuery.class
      );

    return response;
  }
}
