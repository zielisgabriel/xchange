package br.com.xchange.api.domain.ports.services;

import java.util.List;
import java.util.UUID;

public interface AccessTokenServicePort {
  String generate(Object principal);
  String generateFromUserId(UUID userId);
  String getSubject(String token);
  List<Object> getAuthorities(String token);
  boolean validate(String token);
}
