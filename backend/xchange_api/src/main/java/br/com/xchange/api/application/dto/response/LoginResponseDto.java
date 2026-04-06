package br.com.xchange.api.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginResponseDto(
    @JsonProperty(value = "access_token") String accessToken) {
}
