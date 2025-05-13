package com.brkz.demo.controller.dtos.response;

import lombok.Data;

@Data
public class RoleResponseDto {
    private String id;
    private String name;
    private String description;
    private boolean composite;
    private boolean clientRole;
    private String containerId;
} 