package com.brkz.demo.service.impl;

import com.brkz.demo.client.KeycloakRoleClient;
import com.brkz.demo.controller.dtos.request.RoleRequestDto;
import com.brkz.demo.controller.dtos.response.RoleResponseDto;
import com.brkz.demo.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final KeycloakRoleClient keycloakRoleClient;

    @Override
    public RoleResponseDto createRole(RoleRequestDto roleDto) {
        return keycloakRoleClient.createRole(roleDto);
    }

    @Override
    public List<RoleResponseDto> getAllRoles() {
        return keycloakRoleClient.getAllRoles();
    }

    @Override
    public RoleResponseDto getRoleById(String id) {
        return keycloakRoleClient.getRoleById(id);
    }

    @Override
    public RoleResponseDto updateRole(String id, RoleRequestDto roleDto) {
        return keycloakRoleClient.updateRole(id, roleDto);
    }

    @Override
    public void deleteRole(String id) {
        keycloakRoleClient.deleteRole(id);
    }
} 