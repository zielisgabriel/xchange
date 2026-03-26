package br.com.xchange.api.infra.filters;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.xchange.api.domain.exceptions.AccessTokenInvalidException;
import br.com.xchange.api.domain.ports.services.AccessTokenServicePort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccessTokenFilter extends OncePerRequestFilter {
  private final AccessTokenServicePort servicePort;

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {
    String tokenHeader = request.getHeader("Authorization");
    
    if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }
    
    String token = recoverToken(tokenHeader);
    boolean isTokenValid = this.servicePort.validate(token);
    if (!isTokenValid) throw new AccessTokenInvalidException();

    String email = this.servicePort.getSubject(token);
    List<SimpleGrantedAuthority> authorities = this.servicePort.getAuthorities(token)
      .stream()
      .map(authority -> new SimpleGrantedAuthority(authority.toString()))
      .toList();

    SecurityContextHolder.getContext()
      .setAuthentication(new UsernamePasswordAuthenticationToken(
      email,
      null,
      authorities
    ));

    filterChain.doFilter(request, response);
  }

  private String recoverToken(String tokenHeader) {
    return tokenHeader.replace("Bearer ", "");
  }
}
