package com.weescoelho.parkapi.controllers;

import com.weescoelho.parkapi.entities.User;
import com.weescoelho.parkapi.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping
  public ResponseEntity<User> create(@RequestBody User obj){
    User user  = userService.save(obj);
    return ResponseEntity.status(HttpStatus.CREATED).body(user);
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> getById(@PathVariable String id){
    User user = userService.findById(id);
    return ResponseEntity.ok().body(user);
  }

}
