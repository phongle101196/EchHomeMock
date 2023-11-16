package com.mock.service;

import com.mock.entity.User;
import com.mock.payload.request.UserDto;

import java.util.List;

public interface UserService {
    UserDto registerNewUser(UserDto userDto);
    UserDto updateUser(UserDto userDto, Integer userId);
    void toggleUserStatus(int userId);
    UserDto getUserById(Integer userId);
    User findByEmail(String email);

    User saveOrUpdate(User user);

    List<UserDto> getAllUsers();
    void promoteUserToPoster(Integer userId);
    User getUserByUsername(String username);
    User findByResetPasswordToken(String token);

}
