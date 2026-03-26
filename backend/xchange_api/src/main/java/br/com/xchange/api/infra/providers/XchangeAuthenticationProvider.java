package br.com.xchange.api.infra.providers;

import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.xchange.api.domain.exceptions.EmailOrPasswordInvalidException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class XchangeAuthenticationProvider implements AuthenticationProvider {
  private final UserDetailsService userDetailsService;
  private final PasswordEncoder passwordEncoder;
  
  @Override
  public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {
    UserDetails userDetails = this.userDetailsService.loadUserByUsername(authentication.getName());

    boolean isPasswordValid = this.passwordEncoder
      .matches(authentication.getCredentials().toString(), userDetails.getPassword());

    if (!isPasswordValid) {
      throw new EmailOrPasswordInvalidException();
    }

    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
