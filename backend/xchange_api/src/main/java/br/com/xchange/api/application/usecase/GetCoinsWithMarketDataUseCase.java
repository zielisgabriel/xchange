package br.com.xchange.api.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.xchange.api.domain.entities.CoinWithMarketData;
import br.com.xchange.api.domain.ports.services.CoinServicePort;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetCoinsWithMarketDataUseCase {
    private final CoinServicePort coinServicePort;

    public List<CoinWithMarketData> execute() {
        return this.coinServicePort.getCoinsWithMarketData();
    }
}
