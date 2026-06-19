package br.com.xchange.api.integration;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import br.com.xchange.api.domain.entities.Coin;
import br.com.xchange.api.domain.entities.CoinChartData;
import br.com.xchange.api.domain.entities.CoinDetailData;
import br.com.xchange.api.domain.entities.CoinWithMarketData;
import br.com.xchange.api.domain.entities.GlobalCoinMetricsData;

@DisplayName("Integração - CoinsController")
class CoinsControllerIntegrationTest extends AbstractIntegrationTest {

  private String authHeader() {
    return bearerTokenFor(UUID.randomUUID());
  }

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
  @DisplayName("Deve retornar 401 sem token de acesso")
  void shouldRejectUnauthenticated() throws Exception {
    mockMvc.perform(get("/coins/list"))
      .andExpect(status().isUnauthorized());
  }

  @Test
  @DisplayName("Deve retornar 401 com token inválido")
  void shouldRejectInvalidToken() throws Exception {
    mockMvc.perform(get("/coins/list")
        .header(HttpHeaders.AUTHORIZATION, "Bearer token-invalido"))
      .andExpect(status().isUnauthorized());
  }

  @Test
  @DisplayName("GET /coins/list deve retornar a lista de moedas com dados de mercado")
  void shouldReturnCoinsList() throws Exception {
    when(coinServicePort.getCoinsWithMarketData()).thenReturn(List.of(sampleMarketCoin()));

    mockMvc.perform(get("/coins/list").header(HttpHeaders.AUTHORIZATION, authHeader()))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.coins[0].id").value("bitcoin"))
      .andExpect(jsonPath("$.coins[0].price").value("$50,000.00"))
      .andExpect(jsonPath("$.coins[0].priceChangePercentage24h").value(2.5));
  }

  @Test
  @DisplayName("GET /coins/simple deve retornar a lista compacta de moedas")
  void shouldReturnSimpleCoinsList() throws Exception {
    when(coinServicePort.getCoinsWithMarketData()).thenReturn(List.of(sampleMarketCoin()));

    mockMvc.perform(get("/coins/simple").header(HttpHeaders.AUTHORIZATION, authHeader()))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.coins[0].id").value("bitcoin"))
      .andExpect(jsonPath("$.coins[0].symbol").value("btc"));
  }

  @Test
  @DisplayName("GET /coins/trending deve retornar as moedas em tendência")
  void shouldReturnTrendingCoins() throws Exception {
    when(coinServicePort.getTrendingCoinsDetailed()).thenReturn(List.of(sampleMarketCoin()));

    mockMvc.perform(get("/coins/trending").header(HttpHeaders.AUTHORIZATION, authHeader()))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.coins[0].id").value("bitcoin"))
      .andExpect(jsonPath("$.coins[0].marketCap").value("$1,000,000.00"));
  }

  @Test
  @DisplayName("GET /coins/global deve retornar as métricas globais")
  void shouldReturnGlobalMetrics() throws Exception {
    GlobalCoinMetricsData metrics = new GlobalCoinMetricsData();
    metrics.setTotalMarketCap(new BigDecimal("1000"));
    metrics.setTotalVolume(new BigDecimal("200"));
    metrics.setMarketCapChangePercentage24hUsd(new BigDecimal("1.5"));
    metrics.setVolumeChangePercentage24hUsd(new BigDecimal("-0.5"));
    when(coinServicePort.getGlobalCoinMetrics()).thenReturn(metrics);

    mockMvc.perform(get("/coins/global").header(HttpHeaders.AUTHORIZATION, authHeader()))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.data.totalMarketCap").value(1000))
      .andExpect(jsonPath("$.data.totalVolume").value(200));
  }

  @Test
  @DisplayName("GET /coins/chart/{coinId} deve retornar os pontos do gráfico")
  void shouldReturnChartData() throws Exception {
    CoinChartData chartData = new CoinChartData();
    chartData.setPrices(List.of(
      List.of(new BigDecimal("1.0")),
      List.of(new BigDecimal("2.0"))
    ));
    when(coinServicePort.getChartDataById(eq("bitcoin"))).thenReturn(chartData);

    mockMvc.perform(get("/coins/chart/bitcoin").header(HttpHeaders.AUTHORIZATION, authHeader()))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.prices[0][0]").value(1.0))
      .andExpect(jsonPath("$.prices[1][0]").value(2.0));
  }

  @Test
  @DisplayName("GET /coins/detail/{coinId} deve retornar o detalhe da moeda")
  void shouldReturnCoinDetail() throws Exception {
    CoinDetailData detail = new CoinDetailData();
    detail.setId("bitcoin");
    detail.setName("Bitcoin");
    detail.setSymbol("btc");
    detail.setImageUrl("https://img/btc.png");
    detail.setPrice("$50,000.00");
    detail.setDescription("A moeda digital");
    when(coinServicePort.getCoinDetailById(eq("bitcoin"))).thenReturn(detail);

    mockMvc.perform(get("/coins/detail/bitcoin").header(HttpHeaders.AUTHORIZATION, authHeader()))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value("bitcoin"))
      .andExpect(jsonPath("$.description").value("A moeda digital"));
  }

  @Test
  @DisplayName("GET /coins/search deve retornar moedas pela consulta")
  void shouldSearchCoinsByQuery() throws Exception {
    Coin coin = new Coin();
    coin.setId("bitcoin");
    coin.setName("Bitcoin");
    coin.setSymbol("btc");
    coin.setImageUrl("https://img/btc.png");
    when(coinServicePort.getCoinsByQuery(eq("bit"))).thenReturn(List.of(coin));

    mockMvc.perform(get("/coins/search").param("query", "bit")
        .header(HttpHeaders.AUTHORIZATION, authHeader()))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.coins[0].id").value("bitcoin"))
      .andExpect(jsonPath("$.coins[0].name").value("Bitcoin"));
  }
}
