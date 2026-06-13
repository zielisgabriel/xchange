package br.com.xchange.api.infra.providers;

import java.util.Collections;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CoinGeckoRestProvider {
  @Value(value = "${coingecko.api-key}")
  private String apiKey;
  private final String host = "https://api.coingecko.com/api/v3";

  private final RestTemplate restTemplate;

  public <T> @Nullable T getForObject(String path, Class<T> responseType) {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.set("x-cg-demo-api-key", this.apiKey);
    HttpEntity<String> entity = new HttpEntity<>(headers);

    ResponseEntity<T> response = this.restTemplate.exchange(this.host + path, HttpMethod.GET, entity, responseType);
    return response.getBody();
  }
}
