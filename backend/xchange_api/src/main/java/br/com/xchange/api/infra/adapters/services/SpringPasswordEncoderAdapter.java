package br.com.xchange.api.infra.adapters.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.xchange.api.domain.ports.services.PasswordEncoderPort;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SpringPasswordEncoderAdapter implements PasswordEncoderPort {
  private final PasswordEncoder passwordEncoder;

  @Override
  public String encode(String rawPassword) {
    return this.passwordEncoder.encode(rawPassword);
  }
}
