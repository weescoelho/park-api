package com.weescoelho.parkapi.jwt;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.weescoelho.parkapi.entities.User;
import com.weescoelho.parkapi.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtUserDetailService implements UserDetailsService {

  private final UserService userService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userService.findByUsername(username);
    return new JwtUserDetails(user);
  }

  public JwtToken getTokenAuthenticated(String username) {
    User.Role role = userService.findRoleByUsername(username);
    return JwtUtils.createToken(username, role.name());
  }

}
