package com.weescoelho.parkapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDTO {

  @Email(message = "E-mail is not valid!")
  @NotBlank
  private String username;

  @NotBlank
  @Size(min = 6, max = 6)
  private String password;
}
