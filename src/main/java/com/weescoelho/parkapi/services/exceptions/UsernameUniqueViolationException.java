package com.weescoelho.parkapi.services.exceptions;

public class UsernameUniqueViolationException extends RuntimeException {
  public UsernameUniqueViolationException(String message) {
    super(message);
  }
}
