package br.com.xchange.api.application.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.xchange.api.application.dto.CoinWithMarketData;
import br.com.xchange.api.application.dto.response.TrendingCoinResponse;
import br.com.xchange.api.application.dto.response.TrendingCoinResponse.TrendingCoinWrapper;
import br.com.xchange.api.domain.entities.Coin;
import br.com.xchange.api.domain.ports.services.CoinServicePort;

@ExtendWith(MockitoExtension.class)
class GetTrendingCoinUseCaseTest {

  @Mock
  private CoinServicePort coinServicePort;

  @InjectMocks
  private GetTrendingCoinUseCase getTrendingCoinUseCase;

  @Nested
  @DisplayName("Cenários de sucesso")
  class SuccessScenarios {

    @Test
    @DisplayName("Deve retornar uma resposta com as moedas em tendência")
    void shouldReturnTrendingCoinsResponse() {
      Coin bitcoin = createCoin("bitcoin", "Bitcoin", "btc", "https://img.com/btc.png", 100000);
      CoinWithMarketData btcData = new CoinWithMarketData(bitcoin, 1.0, "$2T", "$50B", "sparkline_btc");

      Coin ethereum = createCoin("ethereum", "Ethereum", "eth", "https://img.com/eth.png", 3000);
      CoinWithMarketData ethData = new CoinWithMarketData(ethereum, 0.05, "$400B", "$20B", "sparkline_eth");

      when(coinServicePort.getTrendingCoinsDetailed()).thenReturn(List.of(btcData, ethData));

      TrendingCoinResponse result = getTrendingCoinUseCase.execute();

      assertNotNull(result);
      assertEquals(2, result.coins().size());
    }

    @Test
    @DisplayName("Deve mapear corretamente os campos da moeda para o wrapper")
    void shouldMapCoinFieldsToWrapperCorrectly() {
      Coin bitcoin = createCoin("bitcoin", "Bitcoin", "btc", "https://img.com/btc.png", 100000);
      CoinWithMarketData btcData = new CoinWithMarketData(bitcoin, 1.0, "$2T", "$50B", "sparkline_btc");

      when(coinServicePort.getTrendingCoinsDetailed()).thenReturn(List.of(btcData));

      TrendingCoinResponse result = getTrendingCoinUseCase.execute();

      TrendingCoinWrapper wrapper = result.coins().get(0);
      assertEquals("bitcoin", wrapper.id());
      assertEquals("Bitcoin", wrapper.name());
      assertEquals("btc", wrapper.symbol());
      assertEquals("https://img.com/btc.png", wrapper.imageUrl());
      assertEquals(100000, wrapper.price());
      assertEquals(1.0, wrapper.priceBtc());
      assertEquals("$2T", wrapper.marketCap());
      assertEquals("$50B", wrapper.totalVolume());
      assertEquals("sparkline_btc", wrapper.sparkline());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há moedas em tendência")
    void shouldReturnEmptyListWhenNoTrendingCoins() {
      when(coinServicePort.getTrendingCoinsDetailed()).thenReturn(Collections.emptyList());

      TrendingCoinResponse result = getTrendingCoinUseCase.execute();

      assertNotNull(result);
      assertTrue(result.coins().isEmpty());
    }

    @Test
    @DisplayName("Deve chamar o serviço de moedas exatamente uma vez")
    void shouldCallCoinServiceExactlyOnce() {
      when(coinServicePort.getTrendingCoinsDetailed()).thenReturn(Collections.emptyList());

      getTrendingCoinUseCase.execute();

      verify(coinServicePort, times(1)).getTrendingCoinsDetailed();
    }

    @Test
    @DisplayName("Deve preservar a ordem das moedas retornadas pelo serviço")
    void shouldPreserveOrderFromService() {
      Coin bitcoin = createCoin("bitcoin", "Bitcoin", "btc", "https://img.com/btc.png", 100000);
      Coin ethereum = createCoin("ethereum", "Ethereum", "eth", "https://img.com/eth.png", 3000);
      Coin solana = createCoin("solana", "Solana", "sol", "https://img.com/sol.png", 150);

      List<CoinWithMarketData> serviceData = List.of(
          new CoinWithMarketData(bitcoin, 1.0, "$2T", "$50B", "sparkline_btc"),
          new CoinWithMarketData(ethereum, 0.05, "$400B", "$20B", "sparkline_eth"),
          new CoinWithMarketData(solana, 0.002, "$60B", "$5B", "sparkline_sol")
      );
      when(coinServicePort.getTrendingCoinsDetailed()).thenReturn(serviceData);

      TrendingCoinResponse result = getTrendingCoinUseCase.execute();

      assertEquals("bitcoin", result.coins().get(0).id());
      assertEquals("ethereum", result.coins().get(1).id());
      assertEquals("solana", result.coins().get(2).id());
    }
  }

  private Coin createCoin(String id, String name, String symbol, String imageUrl, Integer price) {
    Coin coin = new Coin();
    coin.setId(id);
    coin.setName(name);
    coin.setSymbol(symbol);
    coin.setImageUrl(imageUrl);
    coin.setPrice(price);
    return coin;
  }
}
