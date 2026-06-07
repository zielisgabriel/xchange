package br.com.xchange.api.application.utils;

import java.security.Principal;
import java.util.UUID;

public class PrincipalUtils {
  public static UUID recoverUserId(Principal principal) {
    return UUID.fromString(principal.getName());
  }
}
