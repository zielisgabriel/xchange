package br.com.xchange.api.infra.handlers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

@Component
public class XchangeAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
      throws IOException, ServletException {
      
    int status = HttpStatus.UNAUTHORIZED.value();
    String error = HttpStatus.UNAUTHORIZED.getReasonPhrase();
    String message = "Você precisa estar autenticado!";
    String path = request.getServletPath();

    Map<String, Object> map = new HashMap<>();
    map.put("timestamp", LocalDateTime.now().toString());
    map.put("status", status);
    map.put("error", error);
    map.put("message", message);
    map.put("path", path);

    response.setStatus(status);
    response.setContentType("application/json;charset=UTF-8");
    response.getWriter().write(new ObjectMapper().writeValueAsString(map));
  }
}
