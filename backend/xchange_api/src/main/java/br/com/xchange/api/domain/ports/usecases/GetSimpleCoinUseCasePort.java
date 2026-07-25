package br.com.xchange.api.domain.ports.usecases;

import java.util.List;

import br.com.xchange.api.domain.entities.Coin;

public interface GetSimpleCoinUseCasePort {
  List<Coin> execute();
}
