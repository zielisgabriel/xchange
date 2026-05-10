package br.com.xchange.api.infra.services.coingecko;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.xchange.api.infra.services.coingecko.dto.GlobalCoinMetrics;
import br.com.xchange.api.infra.services.coingecko.dto.TrendingCoinsCoinsGecko;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CoinsGeckoService {
  private final RestTemplate restTemplate;

  public TrendingCoinsCoinsGecko getTrendingCoins() {
    TrendingCoinsCoinsGecko response = this.restTemplate
      .getForObject("https://api.coingecko.com/api/v3/search/trending", TrendingCoinsCoinsGecko.class);

    return response;
  };

  public GlobalCoinMetrics getGlobalCoinMetrics() {
    GlobalCoinMetrics response = this.restTemplate
      .getForObject("https://api.coingecko.com/api/v3/global", GlobalCoinMetrics.class);

    return response;
  }
}
