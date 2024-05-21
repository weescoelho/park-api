package com.weescoelho.parkapi.controllers.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.fasterxml.jackson.annotation.JsonInclude;

@Getter
@Setter
@NoArgsConstructor
public class StandardError implements Serializable {
  private Long timestamp;
  private Integer status;
  private String error;
  private String message;
  private String path;

  @JsonInclude(JsonInclude.Include.NON_NULL) // Inclui somente se o campo não é nulo
  private Map<String, String> errors;

  public StandardError(Long timestamp, Integer status, String error, String message, String path) {
    this.timestamp = timestamp;
    this.status = status;
    this.error = error;
    this.message = message;
    this.path = path;
  }

  public StandardError(Long timestamp, Integer status, String error, String message, String path,
      BindingResult result) {
    this.timestamp = timestamp;
    this.status = status;
    this.error = error;
    this.message = message;
    this.path = path;
    addErrors(result);
  }

  private void addErrors(BindingResult result) {
    this.errors = new HashMap<>();
    for (FieldError fieldError : result.getFieldErrors()) {
      this.errors.put(fieldError.getField(), fieldError.getDefaultMessage());
    }
  }

}
