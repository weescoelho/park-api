package com.weescoelho.parkapi.services;

import com.weescoelho.parkapi.entities.User;
import com.weescoelho.parkapi.repositories.UserRepository;
import com.weescoelho.parkapi.services.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

  private final UserRepository userRepository;

  @Transactional
  public User save(User data){
    return userRepository.save(data);
  }

  @Transactional(readOnly = true)
  public User findById(String id){
    return userRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("User not found"));
  }
}
