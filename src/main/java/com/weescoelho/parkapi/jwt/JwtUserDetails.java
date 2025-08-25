package com.weescoelho.parkapi.jwt;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class JwtUserDetails extends User {

  private com.weescoelho.parkapi.entities.User user;

  public JwtUserDetails(com.weescoelho.parkapi.entities.User user) {
    super(user.getUsername(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole().name()));
    this.user = user;
  }

  public String getId() {
    return this.user.getId();
  }

  public String getRole() {
    return this.user.getRole().name();
  }

}
