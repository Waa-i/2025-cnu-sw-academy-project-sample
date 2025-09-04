package edu.cnu.swacademy.security.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSignupRequest {

  @NotBlank
  @Size(min = 10, max = 100)
  private String userEmail;

  @NotBlank
  @Size(min = 2, max = 10)
  private String userName;

  @NotBlank
  @Size(min = 8, max = 32)
  private String userPassword;
}
