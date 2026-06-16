package br.com.xchange.api.application.dto.request;

import java.time.LocalDate;

import org.hibernate.validator.constraints.br.CPF;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.xchange.api.domain.entities.AuthUser;
import br.com.xchange.api.domain.valueobject.BirthDate;
import br.com.xchange.api.domain.valueobject.Cpf;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Past;

public record RegisterUserRequest(
  @Email(message = "E-mail invalido!")
  @NotNull(message = "E-mail deve ser preenchido!")
  String email,

  @CPF(message = "CPF inválido!")
  @NotNull(message = "CPF deve ser preenchido!")
  String cpf,
  
  @Size(min = 3, max = 30, message = "O nome tem que ser maior que 3 e menor que 30 caracteres!")
  @JsonProperty(value = "first_name")
  @NotNull(message = "Nome deve ser preenchido!")
  String firstName,

  @Size(min = 3, max = 30, message = "O sobrenome tem que ser maior que 3 e menor que 30 caracteres!")
  @JsonProperty(value = "last_name")
  @NotNull(message = "Sobrenome deve ser preenchido!")
  String lastName,

  @NotNull(message = "Senha deve ser preenchido!")
  @Size(min = 8, max = 100, message = "A senha tem que ser maior que 8 e menor que 100 caracteres!")
  String password,

  @NotNull(message = "Data de aniversário deve ser preenchido!")
  @Past(message = "A data de nascimento tem que ser uma data passada!")
  @JsonProperty(value = "birth_date")
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
