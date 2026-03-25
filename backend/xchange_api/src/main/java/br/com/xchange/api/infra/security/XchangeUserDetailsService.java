package br.com.xchange.api.infra.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.xchange.api.domain.entities.AuthUser;
import br.com.xchange.api.domain.ports.repositories.AuthUserRepositoryPort;
import br.com.xchange.api.infra.entities.AuthUserJpa;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class XchangeUserDetailsService implements UserDetailsService {
  private final AuthUserRepositoryPort repositoryPort;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    AuthUser authUser = this.repositoryPort.findByEmail(email)
      .orElseThrow(() -> new UsernameNotFoundException("Email não encontrado!"));

    UserDetails userDetails = new XchangeUserDetails(AuthUserJpa.fromDomain(authUser));

    return userDetails;
  }
}
