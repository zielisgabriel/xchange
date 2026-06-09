package br.com.xchange.api.infra.adapters.services;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import br.com.xchange.api.application.dto.response.GlobalCoinMetricsResponse;
import br.com.xchange.api.application.dto.response.GlobalCoinMetricsResponse.Data;
import br.com.xchange.api.domain.entities.Coin;
import br.com.xchange.api.domain.entities.CoinChartData;
import br.com.xchange.api.domain.entities.CoinWithMarketData;
import br.com.xchange.api.domain.entities.currencies.Usd;
import br.com.xchange.api.domain.ports.services.CoinServicePort;
import br.com.xchange.api.infra.services.coingecko.CoinsGeckoService;
import br.com.xchange.api.infra.services.coingecko.dto.CoinHistoricalChartData;
import br.com.xchange.api.infra.services.coingecko.dto.CoinInListWithMarketData;
import br.com.xchange.api.infra.services.coingecko.dto.GlobalCoinMetrics;
import br.com.xchange.api.infra.services.coingecko.dto.TrendingCoinsCoinsGecko;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CoinGeckoServiceAdapter implements CoinServicePort {
  private final CoinsGeckoService coinsGeckoService;

  @Override
  public List<CoinWithMarketData> getCoinsWithMarketData() {
    List<CoinInListWithMarketData> data = this.coinsGeckoService.getCoinsListWithMarketData();

    return data.stream()
      .map((item) -> {
        CoinWithMarketData coinWithMarketData = new CoinWithMarketData();
        coinWithMarketData.setId(item.id());
        coinWithMarketData.setName(item.name());
        coinWithMarketData.setSymbol(item.symbol());
        coinWithMarketData.setImageUrl(item.image());
        coinWithMarketData.setPrice(item.currentPrice() != null ? new Usd(item.currentPrice()).formatted(2, 7) : null);
        coinWithMarketData.setMarketCap(item.marketCap() != null ? new Usd(item.marketCap()).formatted(0, 0) : null);
        coinWithMarketData.setTotalVolume(item.totalVolume() != null ? new Usd(item.totalVolume()).formatted(0, 0) : null);
        coinWithMarketData.setPriceChangePercentage24h(item.priceChangePercentage24h());
        coinWithMarketData.setUpdatedAt(item.lastUpdated());

        return coinWithMarketData;
      }).toList();
  }

  @Override
  public List<Coin> getTrendingCoins() {
    TrendingCoinsCoinsGecko data = this.coinsGeckoService.getTrendingCoins();

    if (data == null || data.coins() == null) {
      return Collections.emptyList();
    }

    return data.coins().stream()
      .map(wrapper -> toCoin(wrapper.item()))
      .toList();
  }

  @Override
  public List<CoinWithMarketData> getTrendingCoinsDetailed() {
    TrendingCoinsCoinsGecko data = this.coinsGeckoService.getTrendingCoins();

    if (data == null || data.coins() == null) {
      return Collections.emptyList();
    }

    return data.coins().stream()
      .map(wrapper -> {
        TrendingCoinsCoinsGecko.CoinItem item = wrapper.item();

        CoinWithMarketData coinWithMarketData = new CoinWithMarketData();
        coinWithMarketData.setId(item.id());
        coinWithMarketData.setImageUrl(item.large());
        coinWithMarketData.setMarketCap(item.data().marketCap());
        coinWithMarketData.setName(item.name());
        coinWithMarketData.setPrice(new Usd(item.data().price()).formatted(4, 7));
        coinWithMarketData.setSparkline(item.data().sparkline());
        coinWithMarketData.setSymbol(item.symbol());
        coinWithMarketData.setTotalVolume(item.data().totalVolume());
        coinWithMarketData.setUpdatedAt(LocalDateTime.now().toString());
        coinWithMarketData.setPriceChangePercentage24h(item.data().priceChangePercentage24h().usd());

        return coinWithMarketData;
      }).toList();
  }

  @Override
  public GlobalCoinMetricsResponse getGlobalCoinMetrics() {
    GlobalCoinMetrics data = this.coinsGeckoService.getGlobalCoinMetrics();

    GlobalCoinMetricsResponse.Data dataResponse = new Data(
      data.data().totalMarketCap().usd(),
      data.data().totalVolume().usd(),
      data.data().marketCapChangePercentage24hUsd(),
      data.data().volumeChangePercentage24hUsd()
    );

    return new GlobalCoinMetricsResponse(dataResponse);
  }
  
  @Override
  public CoinChartData getChartDataById(String coinId) {
    CoinHistoricalChartData data = this.coinsGeckoService.getChartDataById(coinId);

    CoinChartData coinChartData = new CoinChartData();
    coinChartData.setPrices(data.prices().subList(data.prices().size() - 5, data.prices().size()));

    return coinChartData;
  }

  private Coin toCoin(TrendingCoinsCoinsGecko.CoinItem item) {
    Coin coin = new Coin();
    coin.setId(item.id());
    coin.setName(item.name());
    coin.setSymbol(item.symbol());
    coin.setImageUrl(item.large());

    if (item.data() != null) {
      coin.setPrice(item.data().price() != null ? new Usd(item.data().price()).formatted(4, 7) : null);
    }

    return coin;
  }
}
