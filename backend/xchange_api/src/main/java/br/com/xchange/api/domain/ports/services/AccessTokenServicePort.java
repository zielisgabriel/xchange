package br.com.xchange.api.domain.ports.services;

import java.util.List;

public interface AccessTokenServicePort {
  String generate(Object principal);
  String getSubject(String token);
  List<Object> getAuthorities(String token);
  boolean validate(String token);
}
