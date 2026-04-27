package br.com.xchange.api.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.xchange.api.application.dto.response.TrendingCoinResponse;
import br.com.xchange.api.application.dto.response.TrendingCoinResponse.TrendingCoinWrapper;
import br.com.xchange.api.domain.ports.services.CoinServicePort;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetTrendingCoinUseCase {
  private final CoinServicePort coinServicePort;

  public TrendingCoinResponse execute() {
    List<TrendingCoinWrapper> coins = this.coinServicePort.getTrendingCoinsDetailed().stream()
      .map(data -> new TrendingCoinResponse.TrendingCoinWrapper(
        data.getId(),
        data.getName(),
        data.getSymbol(),
        data.getImageUrl(),
        data.getPrice(),
        data.getPriceBtc(),
        data.getMarketCap(),
        data.getTotalVolume(),
        data.getSparkline()
      )).toList();

    return new TrendingCoinResponse(coins);
  }
}
