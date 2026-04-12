package br.com.xchange.api.infra.entities;

import java.util.Set;
import java.util.UUID;

import br.com.xchange.api.domain.entities.AuthUser;
import br.com.xchange.api.domain.entities.Profile;
import br.com.xchange.api.domain.valueobject.BirthDate;
import br.com.xchange.api.domain.valueobject.Cpf;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "profiles")
public class ProfileJpa {
  @Id
  @Column(name = "id")
  private UUID id;

  @OneToOne
  @MapsId
  @JoinColumn(name = "id")
  private AuthUserJpa authUserJpa;

  @Column(name = "favorite_cryptos")
  private Set<String> favoriteCryptos;

  public static ProfileJpa fromDomain(Profile profile) {
    ProfileJpa profileJpa = new ProfileJpa();
    profileJpa.setId(profile.getId());
    profileJpa.setFavoriteCryptos(profile.getFavoriteCryptos());
    if (profile.getAuthUser() != null) {
      profileJpa.setAuthUserJpa(AuthUserJpa.fromDomain(profile.getAuthUser()));
    }
    return profileJpa;
  }
  
  public Profile toDomain() {
    Profile profile = new Profile();
    profile.setId(id);
    profile.setFavoriteCryptos(favoriteCryptos);
    if (authUserJpa != null) {
      AuthUser authUser = new AuthUser();
      authUser.setId(authUserJpa.getId());
      authUser.setFirstName(authUserJpa.getFirstName());
      authUser.setLastName(authUserJpa.getLastName());
      authUser.setEmail(authUserJpa.getEmail());
      authUser.setPassword(authUserJpa.getPassword());
      authUser.setBirthDate(new BirthDate(authUserJpa.getBirthDate()));
      authUser.setCpf(new Cpf(authUserJpa.getCpf()));
      profile.setAuthUser(authUser);
    }
    return profile;
  }
}
