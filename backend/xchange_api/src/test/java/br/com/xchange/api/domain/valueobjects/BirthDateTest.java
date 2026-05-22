package br.com.xchange.api.domain.valueobjects;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

// TODO: To finish unit test
public class BirthDateTest {
  @Test
  @DisplayName("Must return the birth date without validate erro")
  void mustReturnTheBirthDateWithoutValidateErro() {
    LocalDate birthDateValid = LocalDate.now().minusYears(19);

    assertEquals(LocalDate.now().minusYears(19).toString(), birthDateValid.toString());
  }
}
