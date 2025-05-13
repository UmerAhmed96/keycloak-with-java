package com.brkz.demo.service;

import com.brkz.demo.controller.dtos.request.UserRequestDto;
import com.brkz.demo.controller.dtos.response.UserResponseDto;

import java.util.List;

public interface UserService {
    UserResponseDto createUser(UserRequestDto dto);
    List<UserResponseDto> getUsers();
    UserResponseDto getUserById(String id);
    void deleteUser(String id);
}
