package br.com.xchange.api.application.dto.request;

import java.time.LocalDate;

import org.hibernate.validator.constraints.br.CPF;

import br.com.xchange.api.domain.entities.AuthUser;
import br.com.xchange.api.domain.valueobject.BirthDate;
import br.com.xchange.api.domain.valueobject.Cpf;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Past;

public record RegisterUserRequestDto(
  @Email(message = "E-mail invalido!")
  String email,
  @CPF(message = "CPF inválido!")
  String cpf,
  @Size(min = 3, max = 30, message = "O nome tem que ser maior que 3 e menor que 30 caracteres!")
  String firstName,
  @Size(min = 3, max = 30, message = "O sobrenome tem que ser maior que 3 e menor que 30 caracteres!")
  String lastName,
  @Size(min = 8, max = 100, message = "A senha tem que ser maior que 8 e menor que 100 caracteres!")
  String password,
  @Past(message = "A data de nascimento tem que ser uma data passada!")
  LocalDate birthDate
) {
  public AuthUser toDomain() {
    AuthUser authUser = new AuthUser();
    authUser.setEmail(email);
    authUser.setFirstName(firstName);
    authUser.setLastName(lastName);
    authUser.setPassword(password);
    authUser.setCpf(new Cpf(cpf));
    authUser.setBirthDate(new BirthDate(birthDate));

    return authUser;
  }
}
