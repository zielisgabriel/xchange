package br.com.xchange.api.infra.entities;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import org.springframework.data.redis.core.index.Indexed;

import br.com.xchange.api.domain.entities.RefreshToken;
import lombok.Data;

@Data
@RedisHash("refresh_token")
public class RefreshTokenRedis {
  public RefreshTokenRedis() {
    this.id = UUID.randomUUID();
    this.expiration = daysInSeconds(15);
  }

  @Id
  private UUID id;

  @Indexed
  private UUID userId;

  @TimeToLive
  private Long expiration;

  private Long daysInSeconds(int days) {
    return Long.valueOf(days * 24 * 60 * 60);
  }

  public static RefreshTokenRedis fromDomain(RefreshToken refreshToken) {
    RefreshTokenRedis refreshTokenRedis = new RefreshTokenRedis();
    refreshTokenRedis.setUserId(refreshToken.getUserId());

    return refreshTokenRedis;
  }

  public RefreshToken toDomain() {
    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setId(this.id);
    refreshToken.setUserId(this.userId);
    refreshToken.setExpiration(this.expiration);

    return refreshToken;
  }
}
