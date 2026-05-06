package br.com.xchange.api.application.dto.response;

import java.util.List;

public record FieldValidationErrorResponse(
  List<FieldErrors> fieldErrors
) {
  public record FieldErrors(
    String field,
    String message
  ) {}
}
