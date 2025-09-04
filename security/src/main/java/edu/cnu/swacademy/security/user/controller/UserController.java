package edu.cnu.swacademy.security.user.controller;

import edu.cnu.swacademy.security.user.dto.UserSignupRequest;
import edu.cnu.swacademy.security.user.dto.UserSignupResponse;
import edu.cnu.swacademy.security.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RequestMapping("/api/v1/user")
@RestController
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
  public UserSignupResponse signUp(@Valid @RequestBody UserSignupRequest userSignupRequest) {
    try {
      int userId = userService.signUp(userSignupRequest);
      return new UserSignupResponse(userId);
    } catch (IllegalArgumentException e) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }
}
