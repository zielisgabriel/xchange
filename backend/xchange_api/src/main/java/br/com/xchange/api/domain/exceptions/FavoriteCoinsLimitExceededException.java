package br.com.xchange.api.domain.exceptions;

public class FavoriteCoinsLimitExceededException extends RuntimeException {
  public FavoriteCoinsLimitExceededException() {
    super("Você atingiu o limite de moedas favoritas!");
  }

  public FavoriteCoinsLimitExceededException(String message) {
    super(message);
  }
}
