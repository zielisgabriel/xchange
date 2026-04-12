package br.com.xchange.api.domain.entities;

import java.util.Set;
import java.util.UUID;

import lombok.Data;

@Data
public class Profile {
  private UUID id;
  private AuthUser authUser;
  private Set<String> favoriteCryptos;
}
