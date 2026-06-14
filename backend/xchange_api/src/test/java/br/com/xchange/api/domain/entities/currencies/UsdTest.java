package br.com.xchange.api.domain.entities.currencies;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UsdTest {

  @Test
  @DisplayName("Deve expor símbolo e código da moeda")
  void shouldExposeSymbolAndCode() {
    Usd usd = new Usd(BigDecimal.ONE);

    assertEquals("$", usd.getSymbol());
    assertEquals("USD", usd.getCode());
  }

  @Test
  @DisplayName("Deve formatar o valor no padrão de moeda dos EUA")
  void shouldFormatUsingUsCurrencyPattern() {
    Usd usd = new Usd(new BigDecimal("1000"));

    assertEquals("$1,000.00", usd.formatted(2, 2));
  }

  @Test
  @DisplayName("Deve interpretar uma string com símbolo e separadores")
  void shouldParseStringWithSymbolAndSeparators() {
    Usd usd = new Usd("$1,234.50");

    assertEquals(0, usd.getValue().compareTo(new BigDecimal("1234.50")));
  }

  @Test
  @DisplayName("Deve preservar o valor recebido como BigDecimal")
  void shouldPreserveBigDecimalValue() {
    BigDecimal value = new BigDecimal("99.99");

    Usd usd = new Usd(value);

    assertEquals(value, usd.getValue());
  }
}
