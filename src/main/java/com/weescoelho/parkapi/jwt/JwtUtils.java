package com.weescoelho.parkapi.jwt;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtUtils {
  public static final String JWT_BEARER = "Bearer ";
  public static final String JWT_AUTHORIZATION = "Authorization";
  public static final String SECRET_KEY = "012345678901234567890123456789012"; // Chave de 256 bits (32 caracteres)
  public static final long EXPIRE_DAYS = 1;
  public static final long EXPIRE_HOURS = 0;
  public static final long EXPIRE_MINUTES = 0;

  private JwtUtils() {
  }

  private static SecretKey generateKey() {
    return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
  }

  private static Date toExpireDate(Date start) {
    LocalDateTime dateTime = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    LocalDateTime end = dateTime.plusDays(EXPIRE_DAYS).plusHours(EXPIRE_HOURS).plusMinutes(EXPIRE_MINUTES);
    return Date.from(end.atZone(ZoneId.systemDefault()).toInstant());
  }

  public static JwtToken createToken(String username, String role) {
    Date issueAt = new Date();
    Date limit = toExpireDate(issueAt);
    String token = Jwts.builder()
        .subject(username)
        .header().add("typ", "JWT").and()
        .subject(username)
        .expiration(limit)
        .signWith(generateKey())
        .claim("role", role)
        .compact();

    return new JwtToken(token);
  }

  private static Claims getClaimsFromToken(String token) {
    try {
      return Jwts.parser()
          .verifyWith(generateKey())
          .build()
          .parseSignedClaims(refactorToken(token))
          .getPayload();
    } catch (JwtException e) {
      log.error(String.format("Token invalido %s", e.getMessage()));
    }

    return null;
  }

  public static String getUsernameFromToken(String token) {
    return getClaimsFromToken(token).getSubject();
  }

  public static boolean isTokenValid(String token) {
    try {
      Jwts.parser()
          .verifyWith(generateKey())
          .build()
          .parseSignedClaims(refactorToken(token));
      return true;
    } catch (JwtException e) {
      log.error(String.format("Token invalido %s", e.getMessage()));
    }

    return false;
  }

  private static String refactorToken(String token) {
    if (token.contains(JWT_BEARER)) {
      return token.substring(JWT_BEARER.length());
    }
    return token;
  }
}
