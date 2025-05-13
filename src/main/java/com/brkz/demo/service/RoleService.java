package com.brkz.demo.service;

import com.brkz.demo.controller.dtos.request.RoleRequestDto;
import com.brkz.demo.controller.dtos.response.RoleResponseDto;

import java.util.List;

public interface RoleService {
    RoleResponseDto createRole(RoleRequestDto roleDto);
    List<RoleResponseDto> getAllRoles();
    RoleResponseDto getRoleById(String id);
    RoleResponseDto updateRole(String id, RoleRequestDto roleDto);
    void deleteRole(String id);
} 