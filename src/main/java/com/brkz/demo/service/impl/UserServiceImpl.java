package com.brkz.demo.service.impl;

import com.brkz.demo.client.KeycloakClient;
import com.brkz.demo.controller.dtos.request.UserRequestDto;
import com.brkz.demo.controller.dtos.response.UserResponseDto;
import com.brkz.demo.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final KeycloakClient keycloakClient;

    public UserServiceImpl(KeycloakClient keycloakClient) {
        this.keycloakClient = keycloakClient;
    }

    @Override
    public UserResponseDto createUser(UserRequestDto dto) {
        return keycloakClient.createUser(dto);
    }

    @Override
    public List<UserResponseDto> getUsers() {
        return keycloakClient.getAllUsers();
    }

    @Override
    public UserResponseDto getUserById(String id) {
        return keycloakClient.getUserById(id);
    }

    @Override
    public void deleteUser(String id) {
        keycloakClient.deleteUser(id);
    }
}

