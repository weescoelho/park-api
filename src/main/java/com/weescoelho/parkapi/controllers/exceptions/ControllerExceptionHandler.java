package com.weescoelho.parkapi.controllers.exceptions;

import com.weescoelho.parkapi.services.exceptions.PasswordInvalidException;

import com.weescoelho.parkapi.services.exceptions.ObjectNotFoundException;
import com.weescoelho.parkapi.services.exceptions.UsernameUniqueViolationException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {
  @ExceptionHandler(ObjectNotFoundException.class)
  public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, HttpServletRequest request) {
    HttpStatus status = HttpStatus.NOT_FOUND;

    StandardError err = new StandardError(
        System.currentTimeMillis(),
        status.value(), status.name(),
        e.getMessage(),
        request.getRequestURI());

    return ResponseEntity.status(status).body(err);
  }

  @ExceptionHandler(PasswordInvalidException.class)
  public ResponseEntity<StandardError> invalidPassword(PasswordInvalidException e, HttpServletRequest request) {
    HttpStatus status = HttpStatus.BAD_REQUEST;

    StandardError err = new StandardError(
        System.currentTimeMillis(),
        status.value(), status.name(),
        e.getMessage(),
        request.getRequestURI());

    return ResponseEntity.status(status).body(err);
  }

  // Tratamento do erro que é gerado atraves da validação Jakarta Bean Validation
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<StandardError> methodArgumentNotValidException(
      MethodArgumentNotValidException e, HttpServletRequest request, BindingResult validationResult) {
    HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;

    log.error("API Error - ", e);
    StandardError err = new StandardError(
        System.currentTimeMillis(),
        status.value(), "ValidationError",
        status.name(),
        request.getRequestURI(), validationResult);

    return ResponseEntity.status(status).body(err);
  }

  @ExceptionHandler(UsernameUniqueViolationException.class)
  public ResponseEntity<StandardError> usernameUniqueViolationException(
      UsernameUniqueViolationException e, HttpServletRequest request) {
    HttpStatus status = HttpStatus.CONFLICT;

    StandardError err = new StandardError(
        System.currentTimeMillis(),
        status.value(), status.name(),
        e.getMessage(),
        request.getRequestURI());

    return ResponseEntity.status(status).body(err);
  }
}
