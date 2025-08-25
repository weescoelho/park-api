package com.weescoelho.parkapi.controllers;

import com.weescoelho.parkapi.controllers.exceptions.StandardError;
import com.weescoelho.parkapi.dto.UserCreateDTO;
import com.weescoelho.parkapi.dto.UserPasswordDTO;
import com.weescoelho.parkapi.dto.UserResponseDTO;
import com.weescoelho.parkapi.dto.mapper.UserMapper;
import com.weescoelho.parkapi.entities.User;
import com.weescoelho.parkapi.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Users")
@RestController
@RequestMapping(value = "api/v1/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @Operation(summary = "Return user by id", responses = {
      @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))),
      @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
  })
  @GetMapping("/{id}")
  public ResponseEntity<UserResponseDTO> getById(@PathVariable String id) {
    User user = userService.findById(id);
    return ResponseEntity.ok().body(UserMapper.toDTO(user));
  }

  @Operation(summary = "Return list of users", responses = {
      @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserResponseDTO.class)))),

  })
  @GetMapping()
  public ResponseEntity<List<UserResponseDTO>> findAll() {
    List<User> list = userService.findAll();
    return ResponseEntity.ok().body(UserMapper.toListDTO(list));
  }

  @Operation(summary = "Create a new user", responses = {
      @ApiResponse(responseCode = "201", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))),
      @ApiResponse(responseCode = "409", description = "User already exits.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
      @ApiResponse(responseCode = "422", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
  })
  @PostMapping
  public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserCreateDTO obj) {
    User user = userService.save(UserMapper.toUser(obj));
    UserResponseDTO userDTO = UserMapper.toDTO(user);
    return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
  }

  @Operation(summary = "Update password", responses = {
      @ApiResponse(responseCode = "204", description = "Senha atualizada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
      @ApiResponse(responseCode = "400", description = "Senha não confere", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
      @ApiResponse(responseCode = "404", description = "Recurso não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
      @ApiResponse(responseCode = "422", description = "Campos inválidos ou mal formatados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
  })
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
