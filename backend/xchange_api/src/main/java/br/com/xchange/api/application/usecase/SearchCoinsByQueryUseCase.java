package br.com.xchange.api.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.xchange.api.domain.entities.Coin;
import br.com.xchange.api.domain.ports.services.CoinServicePort;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchCoinsByQueryUseCase {
  private final CoinServicePort coinServicePort;

  public List<Coin> execute(String query) {
    return this.coinServicePort.getCoinsByQuery(query);
  }
}
