package br.com.xchange.api.infra.services.coingecko;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.xchange.api.infra.services.coingecko.dto.TrendingCoinsCoinsGeckoResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CoinsGeckoService {
  private final RestTemplate restTemplate;

  public TrendingCoinsCoinsGeckoResponse getTrendingCoins() {
    TrendingCoinsCoinsGeckoResponse response = this.restTemplate
      .getForObject("https://api.coingecko.com/api/v3/search/trending", TrendingCoinsCoinsGeckoResponse.class);

    return response;
  };
}
