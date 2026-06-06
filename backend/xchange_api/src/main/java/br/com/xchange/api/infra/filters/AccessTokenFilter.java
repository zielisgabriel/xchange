package br.com.xchange.api.infra.filters;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.xchange.api.application.dto.response.ApiErrorResponse;
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
  private final ObjectMapper objectMapper = new ObjectMapper()
    .registerModule(new JavaTimeModule())
    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

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

    try {
      String token = recoverToken(tokenHeader);

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
    } catch (Exception exception) {
      log.error("Falha na validação do token JWT: {}", exception.getMessage());

      HttpStatus status = HttpStatus.UNAUTHORIZED;

      ApiErrorResponse errorResponse = ApiErrorResponse.of(
        status.value(),
        status.getReasonPhrase(),
        "Access token inválido ou expirado!",
        request.getRequestURI()
      );

      response.setStatus(status.value());
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setCharacterEncoding("UTF-8");
      response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
      return;
    }

    filterChain.doFilter(request, response);
  }

  private String recoverToken(String tokenHeader) {
    return tokenHeader.replace("Bearer ", "");
  }
}
