package br.com.xchange.api.application.usecase;

import org.springframework.stereotype.Service;

import br.com.xchange.api.domain.entities.CoinDetailData;
import br.com.xchange.api.domain.ports.services.CoinServicePort;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetCoinDetailByIdUseCase {
    private final CoinServicePort coinServicePort;

    public CoinDetailData execute(String coinId) {
        return this.coinServicePort.getCoinDetailById(coinId);
    }
}