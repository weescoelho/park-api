package com.weescoelho.parkapi.controllers;

import com.weescoelho.parkapi.dto.UserCreateDTO;
import com.weescoelho.parkapi.dto.UserResponseDTO;
import com.weescoelho.parkapi.dto.mapper.UserMapper;
import com.weescoelho.parkapi.entities.User;
import com.weescoelho.parkapi.services.UserService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping(value = "api/v1/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/{id}")
  public ResponseEntity<User> getById(@PathVariable String id) {
    User user = userService.findById(id);
    return ResponseEntity.ok().body(user);
  }

  @GetMapping()
  public ResponseEntity<List<User>> findAll() {
    List<User> list = userService.findAll();
    return ResponseEntity.ok().body(list);
  }

  @PostMapping
  public ResponseEntity<UserResponseDTO> create(@RequestBody UserCreateDTO obj) {
    User user = userService.save(UserMapper.toUser(obj));
    UserResponseDTO userDTO = UserMapper.toDTO(user);
    return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
  }

  @PutMapping("/{id}")
  public ResponseEntity<User> updatePassword(@PathVariable String id, @RequestBody User entity) {
    User user = userService.updatePassword(id, entity.getPassword());
    return ResponseEntity.ok().body(user);
  }
}
