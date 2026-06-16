package br.com.xchange.api.infra.adapters.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import br.com.xchange.api.domain.entities.Coin;
import br.com.xchange.api.domain.entities.CoinChartData;
import br.com.xchange.api.domain.entities.CoinDetailData;
import br.com.xchange.api.domain.entities.CoinWithMarketData;
import br.com.xchange.api.domain.entities.GlobalCoinMetricsData;
import br.com.xchange.api.domain.entities.currencies.Usd;
import br.com.xchange.api.domain.ports.services.CoinServicePort;
import br.com.xchange.api.infra.services.coingecko.CoinsGeckoService;
import br.com.xchange.api.infra.services.coingecko.dto.CoinDetail;
import br.com.xchange.api.infra.services.coingecko.dto.CoinHistoricalChartData;
import br.com.xchange.api.infra.services.coingecko.dto.CoinInListWithMarketData;
import br.com.xchange.api.infra.services.coingecko.dto.CoinsByQuery;
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
        coinWithMarketData.setSymbol(item.symbol());
        coinWithMarketData.setTotalVolume(item.data().totalVolume());
        coinWithMarketData.setUpdatedAt(LocalDateTime.now().toString());
        coinWithMarketData.setPriceChangePercentage24h(item.data().priceChangePercentage24h().usd());

        return coinWithMarketData;
      }).toList();
  }

  @Override
  public GlobalCoinMetricsData getGlobalCoinMetrics() {
    GlobalCoinMetrics data = this.coinsGeckoService.getGlobalCoinMetrics();

    GlobalCoinMetricsData metrics = new GlobalCoinMetricsData();
    metrics.setTotalMarketCap(data.data().totalMarketCap().usd());
    metrics.setTotalVolume(data.data().totalVolume().usd());
    metrics.setMarketCapChangePercentage24hUsd(data.data().marketCapChangePercentage24hUsd());
    metrics.setVolumeChangePercentage24hUsd(data.data().volumeChangePercentage24hUsd());

    return metrics;
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

  @Override
  public CoinDetailData getCoinDetailById(String coinId) {
    CoinDetail data = this.coinsGeckoService.getCoinDetailById(coinId);

    if (data == null) return null;

    CoinDetail.MarketData md = data.marketData();

    CoinDetailData entity = new CoinDetailData();
    entity.setId(data.id());
    entity.setName(data.name());
    entity.setSymbol(data.symbol());
    entity.setImageUrl(data.image() != null ? data.image().large() : null);
    entity.setUpdatedAt(data.lastUpdated());
    entity.setDescription(data.description() != null ? data.description().en() : null);
    entity.setHashingAlgorithm(data.hashingAlgorithm());
    entity.setGenesisDate(data.genesisDate());
    entity.setMarketCapRank(data.marketCapRank());
    entity.setWatchlistPortfolioUsers(data.watchlistPortfolioUsers());
    entity.setSentimentVotesUpPercentage(data.sentimentVotesUpPercentage());
    entity.setSentimentVotesDownPercentage(data.sentimentVotesDownPercentage());

    if (md != null) {
      entity.setPrice(usdFormatted(md.currentPrice(), 2, 7));
      entity.setMarketCap(usdFormatted(md.marketCap(), 0, 0));
      entity.setTotalVolume(usdFormatted(md.totalVolume(), 0, 0));
      entity.setFullyDilutedValuation(usdFormatted(md.fullyDilutedValuation(), 0, 0));
      entity.setHigh24h(usdFormatted(md.high24h(), 2, 7));
      entity.setLow24h(usdFormatted(md.low24h(), 2, 7));
      entity.setAth(usdFormatted(md.ath(), 2, 7));
      entity.setAtl(usdFormatted(md.atl(), 2, 7));

      entity.setPriceChange24h(md.priceChange24h() != null
        ? new Usd(md.priceChange24h()).formatted(2, 7) : null);

      entity.setAthChangePercentage(usdPercentage(md.athChangePercentage()));
      entity.setAthDate(md.athDate() != null ? md.athDate().get("usd") : null);
      entity.setAtlChangePercentage(usdPercentage(md.atlChangePercentage()));
      entity.setAtlDate(md.atlDate() != null ? md.atlDate().get("usd") : null);

      entity.setPriceChangePercentage1h(usdPercentage(md.priceChangePercentage1hInCurrency()));
      entity.setPriceChangePercentage24h(md.priceChangePercentage24h());
      entity.setPriceChangePercentage7d(md.priceChangePercentage7d());
      entity.setPriceChangePercentage14d(md.priceChangePercentage14d());
      entity.setPriceChangePercentage30d(md.priceChangePercentage30d());
      entity.setPriceChangePercentage60d(md.priceChangePercentage60d());
      entity.setPriceChangePercentage200d(md.priceChangePercentage200d());
      entity.setPriceChangePercentage1y(md.priceChangePercentage1y());

      entity.setCirculatingSupply(md.circulatingSupply());
      entity.setTotalSupply(md.totalSupply());
      entity.setMaxSupply(md.maxSupply());

      entity.setSparkline7d(md.sparkline7d() != null ? md.sparkline7d().price() : null);
    }

    return entity;
  }

  private String usdFormatted(Map<String, BigDecimal> map, int minDecimals, int maxDecimals) {
    if (map == null) return null;
    BigDecimal value = map.get("usd");
    return value != null ? new Usd(value).formatted(minDecimals, maxDecimals) : null;
  }

  private BigDecimal usdPercentage(Map<String, BigDecimal> map) {
    return map != null ? map.get("usd") : null;
  }

  @Override
  public List<Coin> getCoinsByQuery(String query) {
    CoinsByQuery data = this.coinsGeckoService.getCoinsByQuery(query);

    if (data == null || data.coins() == null) {
      return Collections.emptyList();
    }

    return data.coins().stream()
      .limit(20)
      .map(item -> {
        Coin coin = new Coin();
        coin.setId(item.id());
        coin.setName(item.name());
        coin.setSymbol(item.symbol());
        coin.setImageUrl(item.thumb());
        return coin;
      })
      .toList();
  }
}
