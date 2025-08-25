package com.weescoelho.parkapi.services;

import com.weescoelho.parkapi.entities.User;
import com.weescoelho.parkapi.entities.User.Role;
import com.weescoelho.parkapi.repositories.UserRepository;
import com.weescoelho.parkapi.services.exceptions.PasswordInvalidException;

import com.weescoelho.parkapi.services.exceptions.ObjectNotFoundException;
import com.weescoelho.parkapi.services.exceptions.UsernameUniqueViolationException;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public User save(User data) {
    try {
      data.setPassword(passwordEncoder.encode(data.getPassword()));
      return userRepository.save(data);
    } catch (DataIntegrityViolationException e) {
      throw new UsernameUniqueViolationException(String.format("Username {%s} already exists!", data.getUsername()));
    }
  }

  @Transactional(readOnly = true)
  public User findById(String id) {
    return userRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("User not found"));
  }

  @Transactional
  public User updatePassword(String id, String password, String newPassword, String confirmPassword) {

    if (!newPassword.equals(confirmPassword)) {
      throw new PasswordInvalidException("New Password and password confirmation does not match");
    }

    User user = findById(id);

    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new PasswordInvalidException("Invalid password!");
    }

    user.setPassword(passwordEncoder.encode(newPassword));
    return user;
  }

  public List<User> findAll() {
    return userRepository.findAll();
  }

  @Transactional(readOnly = true)
  public User findByUsername(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new ObjectNotFoundException(String.format("User with username '%s' not found", username)));
  }

  public Role findRoleByUsername(String username) {
    return userRepository.findRoleByUsername(username);
  }
}
