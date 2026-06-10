package br.com.xchange.api.infra.config;

import java.time.Duration;

import org.springframework.boot.cache.autoconfigure.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter.TtlFunction;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

@Configuration
public class RedisCacheConfig {
  @Bean
  public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
    return (builder) -> builder
      .withCacheConfiguration("coinsList", RedisCacheConfiguration
        .defaultCacheConfig().entryTtl(TtlFunction.just(Duration.ofMinutes(10)))
        .serializeValuesWith(SerializationPair.fromSerializer(RedisSerializer.json()))
      )
      .withCacheConfiguration("simpleCoinsList", RedisCacheConfiguration
        .defaultCacheConfig().entryTtl(TtlFunction.just(Duration.ofDays(15)))
        .serializeValuesWith(SerializationPair.fromSerializer(RedisSerializer.json()))
      )
      .withCacheConfiguration("trendingCoins", RedisCacheConfiguration
        .defaultCacheConfig().entryTtl(TtlFunction.just(Duration.ofMinutes(10)))
        .serializeValuesWith(SerializationPair.fromSerializer(RedisSerializer.json()))
      )
      .withCacheConfiguration("globalCoinMetrics", RedisCacheConfiguration
        .defaultCacheConfig().entryTtl(TtlFunction.just(Duration.ofHours(4)))
        .serializeValuesWith(SerializationPair.fromSerializer(RedisSerializer.json()))
      )
      .withCacheConfiguration("coinChart", RedisCacheConfiguration
        .defaultCacheConfig().entryTtl(TtlFunction.just(Duration.ofMinutes(5)))
        .serializeValuesWith(SerializationPair.fromSerializer(RedisSerializer.json()))
      )
      .withCacheConfiguration("coinDetail", RedisCacheConfiguration
        .defaultCacheConfig().entryTtl(TtlFunction.just(Duration.ofMinutes(5)))
        .serializeValuesWith(SerializationPair.fromSerializer(RedisSerializer.json()))
      );
  }
}
