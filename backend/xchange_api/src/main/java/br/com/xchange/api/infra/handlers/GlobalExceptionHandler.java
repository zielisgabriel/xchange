package br.com.xchange.api.infra.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import br.com.xchange.api.domain.exceptions.EmailOrPasswordInvalidException;

@ControllerAdvice
public class GlobalExceptionHandler {
  private final String DEFAULT_ERROR_VIEW = "error";

  @ResponseStatus(value = HttpStatus.CONFLICT)
  @ExceptionHandler(value = EmailOrPasswordInvalidException.class)
  public ModelAndView emailOrPasswordInvalidHandler(EmailOrPasswordInvalidException exception) {
    ModelAndView modelAndView = new ModelAndView();

    modelAndView.setStatus(HttpStatus.CONFLICT);
    modelAndView.setViewName(DEFAULT_ERROR_VIEW);
    modelAndView.addObject("message", exception.getMessage());

    return modelAndView;
  }
}
