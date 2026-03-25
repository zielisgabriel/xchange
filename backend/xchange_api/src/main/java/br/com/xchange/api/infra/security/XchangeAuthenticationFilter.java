package br.com.xchange.api.infra.security;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.MimeTypeUtils;

import br.com.xchange.api.application.dto.request.LoginUserRequestDto;
import br.com.xchange.api.application.dto.response.LoginResponseDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

public class XchangeAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
  public XchangeAuthenticationFilter(AuthenticationManager authenticationManager) {
    setAuthenticationManager(authenticationManager);
  }

  @Override
  public Authentication attemptAuthentication(
    HttpServletRequest request,
    HttpServletResponse response
  ) throws AuthenticationException {
    try {
      LoginUserRequestDto loginRequestDto = new ObjectMapper()
        .readValue(request.getInputStream(), LoginUserRequestDto.class);

      UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
        loginRequestDto.email(),
        loginRequestDto.password()
      );

      return getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
    } catch (JacksonException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void successfulAuthentication(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain chain,
    Authentication authResult
  ) throws IOException, ServletException {
    LoginResponseDto loginResponseDto = new LoginResponseDto("access-token-test");

    response.setContentType(MimeTypeUtils.APPLICATION_JSON.getType());
    response.getWriter().write(new ObjectMapper().writeValueAsString(loginResponseDto));
    response.setStatus(200);
  }
}
