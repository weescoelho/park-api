package com.weescoelho.parkapi.dto.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;

import com.weescoelho.parkapi.dto.UserCreateDTO;
import com.weescoelho.parkapi.dto.UserResponseDTO;
import com.weescoelho.parkapi.entities.User;

public class UserMapper {

  public static User toUser(UserCreateDTO createDto) {
    return new ModelMapper().map(createDto, User.class);
  }

  public static UserResponseDTO toDTO(User user) {
    String role = user.getRole().name();
    ModelMapper mapperMain = new ModelMapper();

    TypeMap<User, UserResponseDTO> propertyMapper = mapperMain.createTypeMap(User.class, UserResponseDTO.class);

    propertyMapper.addMappings(
        mapper -> mapper.map(src -> role, UserResponseDTO::setRole));
    return mapperMain.map(user, UserResponseDTO.class);
  }

  public static List<UserResponseDTO> toListDTO(List<User> users) {
    return users.stream().map(user -> toDTO(user)).toList();
  }
}

// Mapeamento de DTO usando bean
/*
 * public Usuario toUsuario(UsuarioCreateDto usuarioCreateDto){
 * Usuario usuario = new Usuario();
 * BeanUtils.copyProperties(usuarioCreateDto, usuario);
 * return usuario;
 * }
 * public UsuarioResponseDto toDto(Usuario usuario){
 * UsuarioResponseDto usuarioResponseDto = new UsuarioResponseDto();
 * BeanUtils.copyProperties(usuario, usuarioResponseDto);
 * usuarioResponseDto.setRole(usuario.getRole().name().substring("ROLE_".length(
 * )));
 * return usuarioResponseDto;
 * }
 */
