package br.com.xchange.api.application.usecase;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.xchange.api.domain.entities.CoinDetailData;
import br.com.xchange.api.domain.ports.services.CoinServicePort;

@ExtendWith(MockitoExtension.class)
class GetCoinDetailByIdUseCaseTest {

  @Mock
  private CoinServicePort coinServicePort;

  @InjectMocks
  private GetCoinDetailByIdUseCase useCase;

  @Test
  @DisplayName("Deve delegar a busca de detalhe ao serviço de moedas")
  void shouldDelegateDetailLookupToService() {
    CoinDetailData detail = new CoinDetailData();
    when(coinServicePort.getCoinDetailById("bitcoin")).thenReturn(detail);

    CoinDetailData result = useCase.execute("bitcoin");

    assertSame(detail, result);
    verify(coinServicePort).getCoinDetailById("bitcoin");
  }
}
