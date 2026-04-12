package br.com.xchange.api.infra.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.xchange.api.application.dto.response.ApiErrorResponse;
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
