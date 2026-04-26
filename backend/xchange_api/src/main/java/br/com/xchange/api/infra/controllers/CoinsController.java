package br.com.xchange.api.infra.controllers;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.xchange.api.application.dto.response.TrendingCoinResponse;
import br.com.xchange.api.application.usecase.GetTrendingCoinUseCase;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coins")
public class CoinsController {
  private final GetTrendingCoinUseCase getTrendingCoinUseCase;

  @Cacheable(value = "coins")
  @ResponseStatus(code = HttpStatus.OK)
  @GetMapping("/trending")
  public List<TrendingCoinResponse> getTrendingCoins() {
    return this.getTrendingCoinUseCase.execute();
  }
}
