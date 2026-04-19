package br.com.xchange.api.infra.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.xchange.api.infra.services.CoinsGeckoService;
import br.com.xchange.api.infra.services.dto.coingecko.TrendingCoinsCoinsGeckoResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coins")
public class CoinsController {
  private final CoinsGeckoService coinsService;

  @ResponseStatus(code = HttpStatus.OK)
  @GetMapping("/trending")
  public TrendingCoinsCoinsGeckoResponse getTrendingCoins() {
    return this.coinsService.getTrendingCoins();
  }
}
