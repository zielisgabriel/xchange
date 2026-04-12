package br.com.xchange.api.infra.entities;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class XchangeUserDetails implements UserDetails {
  private final AuthUserJpa authUserJpa;

  public UUID getId() {
    return authUserJpa.getId();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

  @Override
  public @Nullable String getPassword() {
    return authUserJpa.getPassword();
  }

  @Override
  public String getUsername() {
    return authUserJpa.getEmail();
  }
}
