package br.com.xchange.api.infra.adapters.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import br.com.xchange.api.application.dto.response.GlobalCoinMetricsResponse;
import br.com.xchange.api.application.dto.response.GlobalCoinMetricsResponse.Data;
import br.com.xchange.api.domain.entities.Coin;
import br.com.xchange.api.domain.entities.CoinWithMarketData;
import br.com.xchange.api.domain.ports.services.CoinServicePort;
import br.com.xchange.api.infra.services.coingecko.CoinsGeckoService;
import br.com.xchange.api.infra.services.coingecko.dto.GlobalCoinMetrics;
import br.com.xchange.api.infra.services.coingecko.dto.TrendingCoinsCoinsGecko;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CoinGeckoServiceAdapter implements CoinServicePort {
  private final CoinsGeckoService coinsGeckoService;

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
        coinWithMarketData.setPrice(new BigDecimal(item.data().price().doubleValue()));
        coinWithMarketData.setPriceBtc(item.priceBtc());
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

  private Coin toCoin(TrendingCoinsCoinsGecko.CoinItem item) {
    Coin coin = new Coin();
    coin.setId(item.id());
    coin.setName(item.name());
    coin.setSymbol(item.symbol());
    coin.setImageUrl(item.large());

    if (item.data() != null) {
      coin.setPrice(item.data().price() != null ? new BigDecimal(item.data().price().doubleValue()) : null);
    }

    return coin;
  }
}
