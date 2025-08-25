package com.weescoelho.parkapi.controllers;

import java.sql.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weescoelho.parkapi.controllers.exceptions.StandardError;
import com.weescoelho.parkapi.dto.UserLoginDTO;
import com.weescoelho.parkapi.jwt.JwtToken;
import com.weescoelho.parkapi.jwt.JwtUserDetailService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

  private final JwtUserDetailService detailsService;
  private final AuthenticationManager authenticationManager;

  @PostMapping("/auth")
  private ResponseEntity<?> auth(@Valid @RequestBody UserLoginDTO dto, HttpServletRequest request) {
    log.info("POST /auth - Payload: {}", dto.getPassword());

    try {
      UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(dto.getUsername(),
          dto.getPassword());
      authenticationManager.authenticate(authToken);

      JwtToken token = detailsService.getTokenAuthenticated(dto.getUsername());

      return ResponseEntity.ok(token);
    } catch (AuthenticationException e) {
      log.warn("Bad credentials from username {}", dto.getUsername());
    }

    return ResponseEntity.badRequest()
        .body(new StandardError(Date.valueOf(java.time.LocalDate.now()).getTime(), HttpStatus.BAD_REQUEST.value(),
            "Bad Request", "Invalid username or password", request.getRequestURI()));
  }

}
