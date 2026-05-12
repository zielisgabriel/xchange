package br.com.xchange.api.infra.entities;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.stereotype.Component;

import br.com.xchange.api.domain.entities.AuthUser;
import br.com.xchange.api.domain.entities.Profile;
import br.com.xchange.api.domain.valueobject.BirthDate;
import br.com.xchange.api.domain.valueobject.Cpf;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Component
@Table(name = "auth_users")
public class AuthUserJpa {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @OneToOne(mappedBy = "authUserJpa", cascade = CascadeType.ALL)
  @PrimaryKeyJoinColumn
  private ProfileJpa profileJpa;

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
    if (profileJpa != null) {
      Profile profile = new Profile();
      profile.setId(profileJpa.getId());
      profile.setFavoriteCryptos(profileJpa.getFavoriteCryptos());
      authUser.setProfile(profile);
    }

    return authUser;
  }

  public static AuthUserJpa fromDomain(AuthUser authUser) {
    AuthUserJpa authUserJpa = new AuthUserJpa();

    authUserJpa.setId(authUser.getId());
    authUserJpa.setFirstName(authUser.getFirstName());
    authUserJpa.setLastName(authUser.getLastName());
    authUserJpa.setEmail(authUser.getEmail());
    authUserJpa.setPassword(authUser.getPassword());
    authUserJpa.setBirthDate(authUser.getBirthDate().getValue());
    authUserJpa.setCpf(authUser.getCpf().getValue());

    return authUserJpa;
  }
}
