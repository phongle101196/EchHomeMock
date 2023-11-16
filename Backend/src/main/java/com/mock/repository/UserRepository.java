package com.mock.repository;

import com.mock.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    User findByEmail(String email);

    List<User> findAll();
    User findByUsername(String username);
    User findByResetPasswordToken(String token);
}
