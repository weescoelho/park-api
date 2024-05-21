package com.weescoelho.parkapi.dto;

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
public class UserPasswordDTO {
  @NotBlank
  @Size(min = 6, max = 6)
  private String currentPassword;
  @NotBlank
  @Size(min = 6, max = 6)
  private String newPassword;

  @NotBlank
  @Size(min = 6, max = 6)
  private String confirmPassword;
}
