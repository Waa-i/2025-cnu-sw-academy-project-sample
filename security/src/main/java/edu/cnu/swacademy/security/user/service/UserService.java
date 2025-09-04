package edu.cnu.swacademy.security.user.service;

import edu.cnu.swacademy.security.user.dto.UserSignupRequest;
import edu.cnu.swacademy.security.user.entity.User;
import edu.cnu.swacademy.security.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Transactional
  public int signUp(UserSignupRequest userSignupRequest) {
    boolean existsByEmail = userRepository.existsByEmail(userSignupRequest.getUserEmail());

    if (existsByEmail) {
      throw new IllegalArgumentException("Already exist email.");
    }

    User savedUser = userRepository.save(
        new User(
            userSignupRequest.getUserName(),
            userSignupRequest.getUserEmail(),
            userSignupRequest.getUserPassword()
        )
    );
    log.info("Completed sign up. user-id : {}", savedUser.getId());

    return savedUser.getId();
  }
}
