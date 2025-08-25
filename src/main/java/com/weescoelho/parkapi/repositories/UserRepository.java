package com.weescoelho.parkapi.repositories;

import com.weescoelho.parkapi.entities.User;
import com.weescoelho.parkapi.entities.User.Role;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, String> {
  Optional<User> findByUsername(String username);

  @Query("select u.role from User u where u.username like :username")
  Role findRoleByUsername(String username);
}