package com.weescoelho.parkapi.services;

import com.weescoelho.parkapi.entities.User;
import com.weescoelho.parkapi.repositories.UserRepository;
import com.weescoelho.parkapi.services.exceptions.PasswordInvalidException;

import com.weescoelho.parkapi.services.exceptions.ObjectNotFoundException;
import com.weescoelho.parkapi.services.exceptions.UsernameUniqueViolationException;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public User save(User data) {
    try {
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

    if (!user.getPassword().equals(password)) {
      throw new PasswordInvalidException("Invalid password!");
    }

    user.setPassword(newPassword);
    return user;
  }

  public List<User> findAll() {
    return userRepository.findAll();
  }
}
