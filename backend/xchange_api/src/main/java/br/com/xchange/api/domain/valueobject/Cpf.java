package br.com.xchange.api.domain.valueobject;

import br.com.xchange.api.domain.exceptions.CpfException;
import lombok.Value;

@Value
public class Cpf {
  private static final int CPF_LENGTH = 11;

  private final String value;

  public Cpf(String value) {
    String digits = onlyDigits(value);
    validate(digits);
    this.value = digits;
  }

  private static String onlyDigits(String cpf) {
    if (cpf == null) {
      throw new CpfException("Cpf não pode ser vazio!");
    }
    return cpf.replaceAll("\\D", "");
  }

  private void validate(String cpf) {
    if (cpf.length() != CPF_LENGTH) {
      throw new CpfException("Cpf possui o tamanho inválido!");
    }

    if (cpf.matches("(\\d)\\1{10}")) {
      throw new CpfException("Cpf não pode conter o mesmo dígito!");
    }

    if (checkDigit(cpf, 9) != digitAt(cpf, 9) || checkDigit(cpf, 10) != digitAt(cpf, 10)) {
      throw new CpfException("Cpf inválido!");
    }
  }

  private int checkDigit(String cpf, int length) {
    int sum = 0;
    int weight = length + 1;
    for (int i = 0; i < length; i++) {
      sum += digitAt(cpf, i) * weight--;
    }

    int verifier = 11 - (sum % 11);
    return verifier >= 10 ? 0 : verifier;
  }

  private int digitAt(String cpf, int index) {
    return cpf.charAt(index) - '0';
  }
}
