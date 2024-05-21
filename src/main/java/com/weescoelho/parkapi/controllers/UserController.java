package com.weescoelho.parkapi.controllers;

import com.weescoelho.parkapi.dto.UserCreateDTO;
import com.weescoelho.parkapi.dto.UserPasswordDTO;
import com.weescoelho.parkapi.dto.UserResponseDTO;
import com.weescoelho.parkapi.dto.mapper.UserMapper;
import com.weescoelho.parkapi.entities.User;
import com.weescoelho.parkapi.services.UserService;

import jakarta.validation.Valid;
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
  public ResponseEntity<UserResponseDTO> getById(@PathVariable String id) {
    User user = userService.findById(id);
    return ResponseEntity.ok().body(UserMapper.toDTO(user));
  }

  @GetMapping()
  public ResponseEntity<List<UserResponseDTO>> findAll() {
    List<User> list = userService.findAll();
    return ResponseEntity.ok().body(UserMapper.toListDTO(list));
  }

  @PostMapping
  public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserCreateDTO obj) {
    User user = userService.save(UserMapper.toUser(obj));
    UserResponseDTO userDTO = UserMapper.toDTO(user);
    return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Void> updatePassword(@PathVariable String id, @Valid @RequestBody UserPasswordDTO entity) {
    userService.updatePassword(
        id,
        entity.getCurrentPassword(),
        entity.getNewPassword(),
        entity.getConfirmPassword());
    return ResponseEntity.noContent().build();
  }
}
