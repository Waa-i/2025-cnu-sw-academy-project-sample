package edu.cnu.swacademy.security.user.repository;

import edu.cnu.swacademy.security.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
  boolean existsByEmail(String email);
}
