package br.com.xchange.api.infra.services;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.xchange.api.domain.ports.services.AccessTokenServicePort;
import br.com.xchange.api.infra.entities.XchangeUserDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtTokenService implements AccessTokenServicePort {
  @Value("${jwt.secret-key}")
  private String key;

  @Override
  public String generate(Object principal) {
    SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes());

    XchangeUserDetails userDetails = (XchangeUserDetails) principal;
    
    Date now = new Date();
    Date expiration = new Date(now.getTime() + 10 * 60 * 1000);

    String jwt = Jwts.builder()
      .subject(userDetails.getId().toString())
      .claim("authorities", userDetails.getAuthorities())
      .issuedAt(now)
      .expiration(expiration)
      .signWith(secretKey)
      .compact();

    return jwt;
  }

  @Override
  public String generateFromUserId(UUID userId) {
    SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes());

    Date now = new Date();
    Date expiration = new Date(now.getTime() + 10 * 60 * 1000);

    return Jwts.builder()
      .subject(userId.toString())
      .claim("authorities", List.of())
      .issuedAt(now)
      .expiration(expiration)
      .signWith(secretKey)
      .compact();
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
