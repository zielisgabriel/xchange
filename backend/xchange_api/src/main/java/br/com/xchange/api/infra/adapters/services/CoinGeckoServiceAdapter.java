package br.com.xchange.api.infra.adapters.services;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import br.com.xchange.api.application.dto.CoinWithMarketData;
import br.com.xchange.api.domain.entities.Coin;
import br.com.xchange.api.domain.ports.services.CoinServicePort;
import br.com.xchange.api.infra.services.coingecko.CoinsGeckoService;
import br.com.xchange.api.infra.services.coingecko.dto.TrendingCoinsCoinsGeckoResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CoinGeckoServiceAdapter implements CoinServicePort {
  private final CoinsGeckoService coinsGeckoService;

  @Override
  public List<Coin> getTrendingCoins() {
    TrendingCoinsCoinsGeckoResponse response = this.coinsGeckoService.getTrendingCoins();

    if (response == null || response.coins() == null) {
      return Collections.emptyList();
    }

    return response.coins().stream()
      .map(wrapper -> toCoin(wrapper.item()))
      .toList();
  }

  @Override
  public List<CoinWithMarketData> getTrendingCoinsDetailed() {
    TrendingCoinsCoinsGeckoResponse response = this.coinsGeckoService.getTrendingCoins();

    if (response == null || response.coins() == null) {
      return Collections.emptyList();
    }

    return response.coins().stream()
      .map(wrapper -> {
        TrendingCoinsCoinsGeckoResponse.CoinItem item = wrapper.item();
        Coin coin = toCoin(item);

        return new CoinWithMarketData(
          coin,
          item.priceBtc(),
          item.data() != null ? item.data().marketCap() : null,
          item.data() != null ? item.data().totalVolume() : null,
          item.data() != null ? item.data().sparkline() : null
        );
      }).toList();
  }

  private Coin toCoin(TrendingCoinsCoinsGeckoResponse.CoinItem item) {
    Coin coin = new Coin();
    coin.setId(item.id());
    coin.setName(item.name());
    coin.setSymbol(item.symbol());
    coin.setImageUrl(item.large());

    if (item.data() != null) {
      coin.setPrice(item.data().price() != null ? item.data().price().intValue() : null);
    }

    return coin;
  }
}
