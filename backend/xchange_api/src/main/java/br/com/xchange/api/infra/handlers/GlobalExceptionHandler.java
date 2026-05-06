package br.com.xchange.api.infra.handlers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.xchange.api.application.dto.response.ApiErrorResponse;
import br.com.xchange.api.application.dto.response.FieldValidationErrorResponse;
import br.com.xchange.api.application.dto.response.FieldValidationErrorResponse.FieldErrors;
import br.com.xchange.api.domain.exceptions.EmailOrPasswordInvalidException;
import br.com.xchange.api.domain.exceptions.ForbiddenChangeAnotherUserInfoException;
import br.com.xchange.api.domain.exceptions.UserAlreadyExistsException;
import br.com.xchange.api.domain.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ApiErrorResponse> handleUserNotFound(
      UserNotFoundException exception, HttpServletRequest request) {
    return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage(), request);
  }

  @ExceptionHandler(EmailOrPasswordInvalidException.class)
  public ResponseEntity<ApiErrorResponse> handleEmailOrPasswordInvalid(
      EmailOrPasswordInvalidException exception, HttpServletRequest request) {
    return buildResponse(HttpStatus.CONFLICT, exception.getMessage(), request);
  }

  @ExceptionHandler(ForbiddenChangeAnotherUserInfoException.class)
  public ResponseEntity<ApiErrorResponse> handleForbiddenChange(
      ForbiddenChangeAnotherUserInfoException exception, HttpServletRequest request) {
    return buildResponse(HttpStatus.FORBIDDEN, exception.getMessage(), request);
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<ApiErrorResponse> handleUserAlreadyExists(
      UserAlreadyExistsException exception, HttpServletRequest request) {
    return buildResponse(HttpStatus.CONFLICT, exception.getMessage(), request);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(
    MethodArgumentNotValidException exception,
    HttpServletRequest request
  ) {
    Map<String, Object> response = new HashMap<>();
    int STATUS_VALUE = HttpStatus.UNPROCESSABLE_CONTENT.value();
    
    List<FieldErrors> fieldErros = exception.getFieldErrors().stream()
      .map((error) -> new FieldErrors(error.getField(), error.getDefaultMessage())).toList();
    FieldValidationErrorResponse fieldValidation = new FieldValidationErrorResponse(fieldErros);

    response.put("message", "Erro de validação!");
    response.put("fieldErrors", fieldValidation.fieldErrors());
    response.put("path", request.getRequestURI());
    response.put("status", STATUS_VALUE);

    return ResponseEntity.status(STATUS_VALUE).body(response);
  }

  private ResponseEntity<ApiErrorResponse> buildResponse(
      HttpStatus status, String message, HttpServletRequest request) {
    ApiErrorResponse body = ApiErrorResponse.of(
      status.value(),
      status.getReasonPhrase(),
      message,
      request.getRequestURI()
    );
    return ResponseEntity.status(status).body(body);
  }
}
