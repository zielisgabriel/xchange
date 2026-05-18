package br.com.xchange.api.domain.entities;

import java.util.UUID;

import lombok.Data;

@Data
public class RefreshToken {
  private UUID id;
  private UUID userId;
  private Long expiration;
}
