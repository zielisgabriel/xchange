package br.com.xchange.api.infra.entities;

import java.time.LocalDate;
import java.util.UUID;

import br.com.xchange.api.domain.entities.AuthUser;
import br.com.xchange.api.domain.valueobject.BirthDate;
import br.com.xchange.api.domain.valueobject.Cpf;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "auth_users")
public class AuthUserJpa {
  @Id
  private UUID id;

  @Column(name = "first_name", length = 50, unique = false, nullable = false)
  private String firstName;

  @Column(name = "last_name", length = 50, unique = false, nullable = false)
  private String lastName;

  @Column(name = "email", length = 150, unique = true, nullable = false)
  private String email;

  @Column(name = "password", unique = false, nullable = false)
  private String password;

  @Column(name = "birth_date", unique = false, nullable = false)
  private LocalDate birthDate;

  @Column(name = "cpf", unique = true, nullable = false)
  private String cpf;

  public AuthUser toDomain() {
    AuthUser authUser = new AuthUser();

    authUser.setId(id);
    authUser.setFirstName(firstName);
    authUser.setLastName(lastName);
    authUser.setEmail(email);
    authUser.setPassword(password);
    authUser.setBirthDate(new BirthDate(birthDate));
    authUser.setCpf(new Cpf(cpf));

    return authUser;
  }
}
