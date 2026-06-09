package br.com.xchange.api.infra.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.xchange.api.application.dto.response.CoinChartDataResponse;
import br.com.xchange.api.application.dto.response.CoinsListResponse;
import br.com.xchange.api.application.dto.response.GlobalCoinMetricsResponse;
import br.com.xchange.api.application.dto.response.SimpleCoinsListResponse;
import br.com.xchange.api.application.dto.response.SimpleCoinsListResponse.SimpleCoinsListWrapper;
import br.com.xchange.api.application.dto.response.TrendingCoinResponse;
import br.com.xchange.api.application.dto.response.CoinsListResponse.CoinsListWrapper;
import br.com.xchange.api.application.usecase.GetCoinsWithMarketDataUseCase;
import br.com.xchange.api.application.usecase.GetGlobalCoinMetricsUseCase;
import br.com.xchange.api.application.usecase.GetTrendingCoinUseCase;
import br.com.xchange.api.domain.entities.CoinChartData;
import br.com.xchange.api.domain.entities.CoinWithMarketData;
import br.com.xchange.api.domain.ports.services.CoinServicePort;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coins")
public class CoinsController {
  private final GetTrendingCoinUseCase getTrendingCoinUseCase;
  private final GetGlobalCoinMetricsUseCase getGlobalCoinMetrics;
  private final GetCoinsWithMarketDataUseCase getCoinsListWithMarketData;
  private final CoinServicePort coinServicePort;

  @Cacheable(value = "simpleCoinsList")
  @ResponseStatus(code = HttpStatus.OK)
  @GetMapping("/simple")
  public SimpleCoinsListResponse getSimpleCoinsListResponse() {
    SimpleCoinsListResponse response = new SimpleCoinsListResponse(
      this.getCoinsListWithMarketData.execute()
        .stream()
        .map((item) -> new SimpleCoinsListWrapper(
          item.getId(),
          item.getName(),
          item.getSymbol(),
          item.getImageUrl()
        )).toList()
    );
    return response;
  }

  @Cacheable(value = "coinsList")
  @ResponseStatus(code = HttpStatus.OK)
  @GetMapping("/list")
  public CoinsListResponse getCoinsList() {
    List<CoinWithMarketData> coinWithMarketDataList = this.getCoinsListWithMarketData.execute();

    List<CoinsListWrapper> coins = coinWithMarketDataList.stream()
      .map(coin -> {
        return new CoinsListWrapper(
          coin.getId(),
          coin.getName(),
          coin.getSymbol(),
          coin.getImageUrl(),
          coin.getPrice(),
          coin.getPriceChangePercentage24h()
        );
      }).toList();

    return new CoinsListResponse(coins);
  }

  @Cacheable(value = "trendingCoins")
  @ResponseStatus(code = HttpStatus.OK)
  @GetMapping("/trending")
  public TrendingCoinResponse getTrendingCoins() {
    return this.getTrendingCoinUseCase.execute();
  }

  @Cacheable(value = "globalCoinMetrics")
  @ResponseStatus(code = HttpStatus.OK)
  @GetMapping("/global")
  public GlobalCoinMetricsResponse getGlobalCoinMetrics() {
    return this.getGlobalCoinMetrics.execute();
  }

  @Cacheable(value = "coinChart", key = "#coinId")
  @ResponseStatus(code = HttpStatus.OK)
  @GetMapping("/chart/{coinId}")
  public CoinChartDataResponse getChartDataById(@PathVariable String coinId) {
    CoinChartData coinChartData = this.coinServicePort.getChartDataById(coinId);

    return new CoinChartDataResponse(new ArrayList<>(coinChartData.getPrices()));
  }
}
