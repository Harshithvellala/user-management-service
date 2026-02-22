package com.example.user_management_service.util;

import org.springframework.stereotype.Component;

import com.example.user_management_service.dto.DepartmentResponseDto;
import com.example.user_management_service.dto.ProjectResponseDto;
import com.example.user_management_service.dto.UserResponseDto;
import com.example.user_management_service.model.Department;
import com.example.user_management_service.model.Project;
import com.example.user_management_service.model.User;

@Component
public class DtoMapper {

    public ProjectResponseDto mapProjectToResponse(Project project) {
        ProjectResponseDto responseDto = new ProjectResponseDto();
        responseDto.setId(project.getId());
        responseDto.setName(project.getName());
        responseDto.setDescription(project.getDescription());
        responseDto.setStatus(project.getStatus());
        responseDto.setStartDate(project.getStartDate());
        responseDto.setEndDate(project.getEndDate());
        return responseDto;
    }

    public UserResponseDto mapUserToResponse(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());
        dto.setDepartmentId(user.getDepartment().getId());
        dto.setDepartmentName(user.getDepartment().getName());
        if(user.getProjects() != null) {
            dto.setProjects(user.getProjects().stream().map(this::mapProjectToResponse).collect(java.util.stream.Collectors.toSet()));
        }
        dto.setPayRoll(user.getPayRoll());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }

    public DepartmentResponseDto mapDepartmentToResponse(Department department) {
        DepartmentResponseDto dto = new DepartmentResponseDto();
        dto.setId(department.getId());
        dto.setName(department.getName());
        dto.setDescription(department.getDescription());
        dto.setLocation(department.getLocation());
        return dto;
    }
}
