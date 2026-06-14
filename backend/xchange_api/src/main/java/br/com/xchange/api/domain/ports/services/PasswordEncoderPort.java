package br.com.xchange.api.domain.ports.services;

public interface PasswordEncoderPort {
  String encode(String rawPassword);
}
