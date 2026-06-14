package br.com.xchange.api.domain.valueobjects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.xchange.api.domain.exceptions.BirthDateException;
import br.com.xchange.api.domain.valueobject.BirthDate;

class BirthDateTest {

  @Test
  @DisplayName("Deve aceitar uma data de nascimento de maior de idade")
  void shouldAcceptAdultBirthDate() {
    LocalDate twentyYearsAgo = LocalDate.now().minusYears(20);

    BirthDate birthDate = new BirthDate(twentyYearsAgo);

    assertEquals(twentyYearsAgo, birthDate.getValue());
  }

  @Test
  @DisplayName("Deve aceitar exatamente 18 anos como limite")
  void shouldAcceptExactlyEighteenYears() {
    LocalDate exactlyEighteen = LocalDate.now().minusYears(18);

    BirthDate birthDate = new BirthDate(exactlyEighteen);

    assertEquals(exactlyEighteen, birthDate.getValue());
  }

  @Test
  @DisplayName("Deve rejeitar uma data no futuro")
  void shouldRejectFutureDate() {
    LocalDate tomorrow = LocalDate.now().plusDays(1);

    BirthDateException exception =
        assertThrows(BirthDateException.class, () -> new BirthDate(tomorrow));

    assertEquals("Data de aniversário inválida!", exception.getMessage());
  }

  @Test
  @DisplayName("Deve rejeitar menores de 18 anos")
  void shouldRejectUnderEighteen() {
    LocalDate seventeenYearsAgo = LocalDate.now().minusYears(17);

    BirthDateException exception =
        assertThrows(BirthDateException.class, () -> new BirthDate(seventeenYearsAgo));

    assertEquals("Proibido para menores de 18 anos!", exception.getMessage());
  }
}
