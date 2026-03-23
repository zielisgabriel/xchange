package br.com.xchange.api.domain.valueobject;

import java.time.LocalDate;

import br.com.xchange.api.domain.exceptions.BirthDateException;
import lombok.Data;

@Data
public class BirthDate {
  private final LocalDate value;
  
  public BirthDate(LocalDate birthDate) {
    this.validate(birthDate);
    this.value = birthDate;
  }

  private boolean validate(LocalDate birthDate) {
    if (birthDate.isAfter(LocalDate.now())) {
      throw new BirthDateException("Data de aniversário inválida!");
    }

    if (birthDate.isAfter(LocalDate.now().minusYears(18))) {
      throw new BirthDateException("Proibido para menores de 18 anos!");
    }

    return true;
  }
}
