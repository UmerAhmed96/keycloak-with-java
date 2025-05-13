package com.brkz.demo.controller.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleRequestDto {
    @NotBlank(message = "Role name is required")
    private String name;
    
    private String description;
    
    private boolean composite;
    
    private boolean clientRole;
} 