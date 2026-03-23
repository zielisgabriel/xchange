package br.com.xchange.api.domain.valueobject;

import br.com.xchange.api.domain.exceptions.CpfException;
import lombok.Data;

@Data
public class Cpf {
    private final String value;

    public Cpf(String value) {
      this.validate(value);
      this.value = value.replaceAll("\\D", "");
    }

    private boolean validate(String cpf) {
      if (cpf == null) {
        throw new CpfException("Cpf não pode ser vazio!");
      }

      cpf = cpf.replaceAll("\\D", "");

      if (cpf.length() != 11) {
        throw new CpfException("Cpf possui o tamanho inválido!");
      }

      if (cpf.matches("(\\d)\\1{10}")) {
        throw new CpfException("Cpf não pode conter o mesmo dígito!");
      }

      try {
        int sum = 0;
        int weight = 10;
        for (int i = 0; i < 9; i++) {
            sum += (cpf.charAt(i) - '0') * weight--;
        }

        int firstVerifier = 11 - (sum % 11);
        if (firstVerifier >= 10) firstVerifier = 0;

        if (firstVerifier != (cpf.charAt(9) - '0')) {
          throw new CpfException("Cpf inválido!");
        }

        sum = 0;
        weight = 11;
        for (int i = 0; i < 10; i++) {
          sum += (cpf.charAt(i) - '0') * weight--;
        }

        int secondVerifier = 11 - (sum % 11);
        if (secondVerifier >= 10) secondVerifier = 0;

        if (secondVerifier != (cpf.charAt(10) - '0')) {
          throw new CpfException("Cpf inválido!");
        }

        return true;
      } catch (Exception e) {
        throw new CpfException("Cpf inválido!");
      }
    }
}
