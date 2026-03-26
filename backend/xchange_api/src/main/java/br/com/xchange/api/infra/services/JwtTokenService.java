package br.com.xchange.api.infra.services;

import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import br.com.xchange.api.domain.ports.services.AccessTokenServicePort;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtTokenService implements AccessTokenServicePort {
  @Value("${jwt.secret-key}")
  private String key;

  @Override
  public String generate(Object principal) {
    SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes());

    UserDetails userDetails = (UserDetails) principal;
    
    String jwt = Jwts.builder()
      .subject(userDetails.getUsername())
      .claim("authorities", userDetails.getAuthorities())
      .signWith(secretKey)
      .compact();

    return jwt;
  }

  @Override
  public String getSubject(String token) {
    SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes());

    return Jwts.parser()
      .verifyWith(secretKey)
      .build()
      .parseSignedClaims(token)
      .getPayload()
      .getSubject();
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<Object> getAuthorities(String token) {
    SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes());

    return Jwts.parser()
      .verifyWith(secretKey)
      .build()
      .parseSignedClaims(token)
      .getPayload()
      .get("authorities", List.class);
  }

  @Override
  public boolean validate(String token) {
    SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes());

    try {
      Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
