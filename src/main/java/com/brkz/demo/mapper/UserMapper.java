package com.brkz.demo.mapper;

import com.brkz.demo.controller.dtos.request.UserRequestDto;
import com.brkz.demo.controller.dtos.response.UserResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    
    @Mapping(target = "id", ignore = true)
    UserResponseDto toResponseDto(UserRequestDto requestDto);
    
    @Mapping(target = "id", ignore = true)
    void updateResponseDtoFromRequestDto(UserRequestDto requestDto, @MappingTarget UserResponseDto responseDto);
    
    UserRequestDto toRequestDto(UserResponseDto responseDto);
} 