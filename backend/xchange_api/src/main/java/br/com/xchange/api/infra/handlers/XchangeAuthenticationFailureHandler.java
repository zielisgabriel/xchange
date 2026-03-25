package br.com.xchange.api.infra.handlers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

@Component
public class XchangeAuthenticationFailureHandler implements AuthenticationFailureHandler {
  @Override
  public void onAuthenticationFailure(
    HttpServletRequest request,
    HttpServletResponse response,
    AuthenticationException exception
  ) throws IOException, ServletException {
    String timestamp = LocalDateTime.now().toString();
    int status = HttpStatus.CONFLICT.value();
    String error = HttpStatus.CONFLICT.getReasonPhrase();
    String message = exception.getMessage();
    String path = request.getServletPath();

    Map<String, Object> map = new HashMap<>();
    map.put("timestamp", timestamp);
    map.put("status", status);
    map.put("error", error);
    map.put("message", message);
    map.put("path", path);

    response.setStatus(status);
    response.setContentType("application/json;charset=UTF-8");
    response.getWriter().write(new ObjectMapper().writeValueAsString(map));
  }
}
