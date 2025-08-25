package com.weescoelho.parkapi.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.weescoelho.parkapi.entities.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

  private JwtUserDetailService userDetailService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    final String token = request.getHeader(JwtUtils.JWT_AUTHORIZATION);
    if (token == null || !token.startsWith(JwtUtils.JWT_BEARER)) {
      log.info("JWT is null or empty or does not start with Bearer");
      filterChain.doFilter(request, response);
      return;
    }

    if (!JwtUtils.isTokenValid(token)) {
      log.warn("Invalid JWT token");
      filterChain.doFilter(request, response);
      return;
    }

    String username = JwtUtils.getUsernameFromToken(token);
    toAuth(request, username);

    filterChain.doFilter(request, response);
    return;
  }

  private void toAuth(HttpServletRequest request, String username) {
    UserDetails userDetails = userDetailService.loadUserByUsername(username);

    UsernamePasswordAuthenticationToken authToken = UsernamePasswordAuthenticationToken
        .authenticated((User) userDetails, null, userDetails.getAuthorities());

    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(authToken);
  }

}
