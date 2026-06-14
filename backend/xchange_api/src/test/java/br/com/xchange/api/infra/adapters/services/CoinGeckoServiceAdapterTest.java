package br.com.xchange.api.infra.adapters.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.xchange.api.domain.entities.Coin;
import br.com.xchange.api.domain.entities.CoinChartData;
import br.com.xchange.api.domain.entities.CoinDetailData;
import br.com.xchange.api.domain.entities.CoinWithMarketData;
import br.com.xchange.api.domain.entities.GlobalCoinMetricsData;
import br.com.xchange.api.infra.services.coingecko.CoinsGeckoService;
import br.com.xchange.api.infra.services.coingecko.dto.CoinDetail;
import br.com.xchange.api.infra.services.coingecko.dto.CoinHistoricalChartData;
import br.com.xchange.api.infra.services.coingecko.dto.CoinInListWithMarketData;
import br.com.xchange.api.infra.services.coingecko.dto.CoinsByQuery;
import br.com.xchange.api.infra.services.coingecko.dto.GlobalCoinMetrics;

@ExtendWith(MockitoExtension.class)
class CoinGeckoServiceAdapterTest {

  @Mock
  private CoinsGeckoService coinsGeckoService;

  @InjectMocks
  private CoinGeckoServiceAdapter adapter;

  private CoinInListWithMarketData marketCoin(String id, BigDecimal currentPrice) {
    return new CoinInListWithMarketData(
        id, id.substring(0, 1), id, "https://img/" + id + ".png",
        currentPrice, new BigDecimal("123"), null, null, new BigDecimal("456"),
        null, null, null, new BigDecimal("2.5"), null, null,
        null, null, null, null, null, null, null, null, null, "2026-06-14");
  }

  @Test
  @DisplayName("Deve mapear moedas com dados de mercado formatando os valores em USD")
  void shouldMapMarketDataFormattingUsd() {
    when(coinsGeckoService.getCoinsListWithMarketData())
        .thenReturn(List.of(marketCoin("bitcoin", new BigDecimal("50000"))));

    List<CoinWithMarketData> result = adapter.getCoinsWithMarketData();

    assertEquals(1, result.size());
    CoinWithMarketData coin = result.get(0);
    assertEquals("bitcoin", coin.getId());
    assertEquals("$50,000.00", coin.getPrice());
    assertEquals(new BigDecimal("2.5"), coin.getPriceChangePercentage24h());
  }

  @Test
  @DisplayName("Deve manter preço nulo quando a API não retorna current_price")
  void shouldKeepNullPriceWhenMissing() {
    when(coinsGeckoService.getCoinsListWithMarketData())
        .thenReturn(List.of(marketCoin("bitcoin", null)));

    List<CoinWithMarketData> result = adapter.getCoinsWithMarketData();

    assertNull(result.get(0).getPrice());
  }

  @Test
  @DisplayName("Deve retornar lista vazia de tendências quando a API retorna nulo")
  void shouldReturnEmptyTrendingWhenNull() {
    when(coinsGeckoService.getTrendingCoins()).thenReturn(null);

    assertTrue(adapter.getTrendingCoins().isEmpty());
    assertTrue(adapter.getTrendingCoinsDetailed().isEmpty());
  }

  @Test
  @DisplayName("Deve mapear as métricas globais para a entidade de domínio")
  void shouldMapGlobalMetricsToDomain() {
    GlobalCoinMetrics.Data data = new GlobalCoinMetrics.Data(
        new GlobalCoinMetrics.Data.TotalMarketCap(new BigDecimal("1000")),
        new GlobalCoinMetrics.Data.TotalVolume(new BigDecimal("200")),
        new BigDecimal("1.5"),
        new BigDecimal("-0.5"));
    when(coinsGeckoService.getGlobalCoinMetrics()).thenReturn(new GlobalCoinMetrics(data));

    GlobalCoinMetricsData metrics = adapter.getGlobalCoinMetrics();

    assertEquals(new BigDecimal("1000"), metrics.getTotalMarketCap());
    assertEquals(new BigDecimal("200"), metrics.getTotalVolume());
    assertEquals(new BigDecimal("1.5"), metrics.getMarketCapChangePercentage24hUsd());
    assertEquals(new BigDecimal("-0.5"), metrics.getVolumeChangePercentage24hUsd());
  }

  @Test
  @DisplayName("Deve retornar apenas os 5 últimos pontos do gráfico")
  void shouldReturnLastFiveChartPoints() {
    List<List<BigDecimal>> prices = new ArrayList<>();
    for (int i = 1; i <= 7; i++) {
      prices.add(List.of(BigDecimal.valueOf(i)));
    }
    when(coinsGeckoService.getChartDataById("bitcoin"))
        .thenReturn(new CoinHistoricalChartData(prices, null, null));

    CoinChartData result = adapter.getChartDataById("bitcoin");

    assertEquals(5, result.getPrices().size());
    assertEquals(BigDecimal.valueOf(3), result.getPrices().get(0).get(0));
    assertEquals(BigDecimal.valueOf(7), result.getPrices().get(4).get(0));
  }

  @Test
  @DisplayName("Deve limitar a busca por consulta a 20 resultados")
  void shouldLimitQueryResultsToTwenty() {
    List<CoinsByQuery.CoinByQuery> coins = IntStream.range(0, 25)
        .mapToObj(i -> new CoinsByQuery.CoinByQuery("id" + i, "name" + i, "s" + i, "thumb" + i))
        .toList();
    when(coinsGeckoService.getCoinsByQuery("coin")).thenReturn(new CoinsByQuery(coins));

    List<Coin> result = adapter.getCoinsByQuery("coin");

    assertEquals(20, result.size());
  }

  @Test
  @DisplayName("Deve retornar lista vazia de busca quando a API retorna nulo")
  void shouldReturnEmptyQueryWhenNull() {
    when(coinsGeckoService.getCoinsByQuery("coin")).thenReturn(null);

    assertTrue(adapter.getCoinsByQuery("coin").isEmpty());
  }

  @Test
  @DisplayName("Deve retornar nulo quando o detalhe da moeda não existe")
  void shouldReturnNullWhenDetailMissing() {
    when(coinsGeckoService.getCoinDetailById("unknown")).thenReturn(null);

    assertNull(adapter.getCoinDetailById("unknown"));
  }

  @Test
  @DisplayName("Deve mapear o detalhe da moeda formatando o preço em USD")
  void shouldMapCoinDetailFormattingPrice() {
    CoinDetail.MarketData marketData = new CoinDetail.MarketData(
        Map.of("usd", new BigDecimal("50000")),
        null, null, null, null, null, null, null, null, null, null, null, null,
        null, new BigDecimal("2.5"), null, null, null, null, null, null,
        null, null, null, null, null, null, null, "2026-06-14");

    CoinDetail detail = new CoinDetail(
        "bitcoin", "btc", "Bitcoin",
        new CoinDetail.ImageData(null, null, "https://img/btc.png"),
        new CoinDetail.Description("A coin"),
        "SHA-256", "2009-01-03",
        new BigDecimal("80"), new BigDecimal("20"), 100L, 1,
        marketData, "2026-06-14");
    when(coinsGeckoService.getCoinDetailById("bitcoin")).thenReturn(detail);

    CoinDetailData result = adapter.getCoinDetailById("bitcoin");

    assertEquals("bitcoin", result.getId());
    assertEquals("https://img/btc.png", result.getImageUrl());
    assertEquals("A coin", result.getDescription());
    assertEquals("$50,000.00", result.getPrice());
    assertEquals(new BigDecimal("2.5"), result.getPriceChangePercentage24h());
  }
}
