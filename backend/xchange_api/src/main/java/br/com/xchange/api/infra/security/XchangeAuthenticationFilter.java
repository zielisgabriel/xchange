package br.com.xchange.api.infra.security;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.xchange.api.application.dto.request.LoginRequestDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
public class XchangeAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
  private final AuthenticationManager authenticationManager;

  @Override
  public Authentication attemptAuthentication(
    HttpServletRequest request,
    HttpServletResponse response
  ) throws AuthenticationException {
    try {
      LoginRequestDto loginRequestDto = new ObjectMapper()
        .readValue(request.getInputStream(), LoginRequestDto.class);

      UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
        loginRequestDto.email(),
        loginRequestDto.password()
      );

      return this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    } catch (JacksonException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
      Authentication authResult) throws IOException, ServletException {
    // TODO Auto-generated method stub
    super.successfulAuthentication(request, response, chain, authResult);
  }
}
