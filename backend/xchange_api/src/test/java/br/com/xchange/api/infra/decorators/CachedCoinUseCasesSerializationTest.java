package br.com.xchange.api.infra.decorators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.data.redis.serializer.RedisSerializer;

import br.com.xchange.api.domain.entities.Coin;
import br.com.xchange.api.domain.entities.CoinWithMarketData;
import br.com.xchange.api.domain.ports.usecases.GetCoinsListUseCasePort;
import br.com.xchange.api.domain.ports.usecases.GetSimpleCoinUseCasePort;

class CachedCoinUseCasesSerializationTest {
  private final RedisSerializer<Object> serializer = RedisSerializer.json();

  @Test
  void shouldRoundTripSimpleCoinsThroughRedisSerializer() {
    Coin bitcoin = new Coin();
    bitcoin.setId("bitcoin");
    bitcoin.setName("Bitcoin");
    bitcoin.setSymbol("btc");

    GetSimpleCoinUseCasePort delegate = () -> List.of(bitcoin);
    List<Coin> cacheValue = new GetSimpleCoinUseCaseCached(delegate).execute();

    assertInstanceOf(ArrayList.class, cacheValue);

    Object restored = serializer.deserialize(serializer.serialize(cacheValue));
    List<?> restoredCoins = assertInstanceOf(List.class, restored);
    Coin restoredBitcoin = assertInstanceOf(Coin.class, restoredCoins.getFirst());

    assertEquals(bitcoin, restoredBitcoin);
  }

  @Test
  void shouldRoundTripCoinsWithMarketDataThroughRedisSerializer() {
    CoinWithMarketData bitcoin = new CoinWithMarketData();
    bitcoin.setId("bitcoin");
    bitcoin.setName("Bitcoin");
    bitcoin.setSymbol("btc");
    bitcoin.setMarketCap("$1.2T");
    bitcoin.setPriceChangePercentage24h(BigDecimal.valueOf(1.25));

    GetCoinsListUseCasePort delegate = () -> List.of(bitcoin);
    List<CoinWithMarketData> cacheValue = new GetCoinsListUseCaseCached(delegate).execute();

    assertInstanceOf(ArrayList.class, cacheValue);

    Object restored = serializer.deserialize(serializer.serialize(cacheValue));
    List<?> restoredCoins = assertInstanceOf(List.class, restored);
    CoinWithMarketData restoredBitcoin = assertInstanceOf(CoinWithMarketData.class, restoredCoins.getFirst());

    assertEquals(bitcoin, restoredBitcoin);
  }
}
