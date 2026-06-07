package br.com.xchange.api.application.dto.request;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;

public record RefreshTokenRequestDto(
    @NotNull(message = "O refresh token é obrigatório!")
    @JsonProperty("refresh_token")
    UUID refreshToken
) {}
