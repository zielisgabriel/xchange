package br.com.xchange.api.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginResponse(
    @JsonProperty(value = "access_token")
    String accessToken,

    @JsonProperty(value = "refresh_token")
    String refreshToken
) {}
