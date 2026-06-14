package br.com.xchange.api.domain.valueobjects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.xchange.api.domain.exceptions.CpfException;
import br.com.xchange.api.domain.valueobject.Cpf;

class CpfTest {

  @Test
  @DisplayName("Deve criar um CPF válido e armazenar apenas os dígitos")
  void shouldCreateValidCpf() {
    Cpf cpf = new Cpf("11144477735");

    assertEquals("11144477735", cpf.getValue());
  }

  @Test
  @DisplayName("Deve normalizar removendo pontuação de um CPF válido")
  void shouldStripPunctuation() {
    Cpf cpf = new Cpf("111.444.777-35");

    assertEquals("11144477735", cpf.getValue());
  }

  @Test
  @DisplayName("Deve rejeitar CPF nulo")
  void shouldRejectNull() {
    CpfException exception = assertThrows(CpfException.class, () -> new Cpf(null));

    assertEquals("Cpf não pode ser vazio!", exception.getMessage());
  }

  @Test
  @DisplayName("Deve rejeitar CPF com tamanho inválido")
  void shouldRejectInvalidLength() {
    CpfException exception = assertThrows(CpfException.class, () -> new Cpf("123"));

    assertEquals("Cpf possui o tamanho inválido!", exception.getMessage());
  }

  @Test
  @DisplayName("Deve rejeitar CPF com todos os dígitos iguais")
  void shouldRejectRepeatedDigits() {
    CpfException exception = assertThrows(CpfException.class, () -> new Cpf("11111111111"));

    assertEquals("Cpf não pode conter o mesmo dígito!", exception.getMessage());
  }

  @Test
  @DisplayName("Deve rejeitar CPF com primeiro dígito verificador inválido")
  void shouldRejectInvalidFirstVerifier() {
    assertThrows(CpfException.class, () -> new Cpf("11144477725"));
  }

  @Test
  @DisplayName("Deve rejeitar CPF com segundo dígito verificador inválido")
  void shouldRejectInvalidSecondVerifier() {
    assertThrows(CpfException.class, () -> new Cpf("11144477736"));
  }
}
