package br.com.xchange.api.e2e;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jayway.jsonpath.JsonPath;

import br.com.xchange.api.domain.entities.Coin;
import br.com.xchange.api.domain.entities.CoinChartData;
import br.com.xchange.api.domain.entities.CoinDetailData;
import br.com.xchange.api.domain.entities.CoinWithMarketData;
import br.com.xchange.api.domain.entities.GlobalCoinMetricsData;

@DisplayName("E2E - Jornada de consulta de moedas")
class CoinsJourneyE2ETest extends AbstractE2ETest {

  private CoinWithMarketData sampleMarketCoin() {
    CoinWithMarketData coin = new CoinWithMarketData();
    coin.setId("bitcoin");
    coin.setName("Bitcoin");
    coin.setSymbol("btc");
    coin.setImageUrl("https://img/btc.png");
    coin.setPrice("$50,000.00");
    coin.setMarketCap("$1,000,000.00");
    coin.setTotalVolume("$200,000.00");
    coin.setPriceChangePercentage24h(new BigDecimal("2.5"));
    return coin;
  }

  @Test
  @DisplayName("Deve consultar todos os endpoints de moedas após autenticar")
  void shouldBrowseAllCoinEndpointsAfterLogin() {
    Tokens tokens = registerAndLogin("moedas@xchange.com");
    String token = tokens.accessToken();

    when(coinServicePort.getCoinsWithMarketData()).thenReturn(List.of(sampleMarketCoin()));
    when(coinServicePort.getTrendingCoinsDetailed()).thenReturn(List.of(sampleMarketCoin()));

    GlobalCoinMetricsData metrics = new GlobalCoinMetricsData();
    metrics.setTotalMarketCap(new BigDecimal("1000"));
    metrics.setTotalVolume(new BigDecimal("200"));
    metrics.setMarketCapChangePercentage24hUsd(new BigDecimal("1.5"));
    metrics.setVolumeChangePercentage24hUsd(new BigDecimal("-0.5"));
    when(coinServicePort.getGlobalCoinMetrics()).thenReturn(metrics);

    CoinChartData chart = new CoinChartData();
    chart.setPrices(List.of(List.of(new BigDecimal("1.0")), List.of(new BigDecimal("2.0"))));
    when(coinServicePort.getChartDataById(eq("bitcoin"))).thenReturn(chart);

    CoinDetailData detail = new CoinDetailData();
    detail.setId("bitcoin");
    detail.setName("Bitcoin");
    detail.setSymbol("btc");
    detail.setDescription("A moeda digital");
    when(coinServicePort.getCoinDetailById(eq("bitcoin"))).thenReturn(detail);

    Coin queried = new Coin();
    queried.setId("bitcoin");
    queried.setName("Bitcoin");
    queried.setSymbol("btc");
    when(coinServicePort.getCoinsByQuery(eq("bit"))).thenReturn(List.of(queried));

    HttpResult list = get("/coins/list", token);
    assertEquals(200, list.status());
    assertEquals("bitcoin", JsonPath.read(list.body(), "$.coins[0].id"));

    HttpResult simple = get("/coins/simple", token);
    assertEquals(200, simple.status());
    assertEquals("btc", JsonPath.read(simple.body(), "$.coins[0].symbol"));

    HttpResult trending = get("/coins/trending", token);
    assertEquals(200, trending.status());
    assertEquals("bitcoin", JsonPath.read(trending.body(), "$.coins[0].id"));

    HttpResult global = get("/coins/global", token);
    assertEquals(200, global.status());
    int totalMarketCap = JsonPath.read(global.body(), "$.data.totalMarketCap");
    assertEquals(1000, totalMarketCap);

    HttpResult chartResponse = get("/coins/chart/bitcoin", token);
    assertEquals(200, chartResponse.status());

    HttpResult detailResponse = get("/coins/detail/bitcoin", token);
    assertEquals(200, detailResponse.status());
    assertEquals("A moeda digital", JsonPath.read(detailResponse.body(), "$.description"));

    HttpResult search = get("/coins/search?query=bit", token);
    assertEquals(200, search.status());
    assertEquals("bitcoin", JsonPath.read(search.body(), "$.coins[0].id"));
  }

  @Test
  @DisplayName("Deve negar consulta de moedas sem autenticação (401)")
  void shouldRejectCoinsWithoutAuthentication() {
    HttpResult response = get("/coins/list", null);

    assertEquals(401, response.status());
  }
}
