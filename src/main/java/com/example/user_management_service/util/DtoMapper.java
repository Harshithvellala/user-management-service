package com.example.user_management_service.util;

import org.springframework.stereotype.Component;

import com.example.user_management_service.dto.DepartmentRequestDto;
import com.example.user_management_service.dto.DepartmentResponseDto;
import com.example.user_management_service.dto.ProjectRequestDto;
import com.example.user_management_service.dto.ProjectResponseDto;
import com.example.user_management_service.dto.UserRequestDto;
import com.example.user_management_service.dto.UserResponseDto;
import com.example.user_management_service.exception.ResourceNotFoundException;
import com.example.user_management_service.model.Department;
import com.example.user_management_service.model.Project;
import com.example.user_management_service.model.User;
import com.example.user_management_service.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class DtoMapper {

    @Autowired
    private DepartmentRepository departmentRepository;

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
    
    public Project mapProjectToEntity(ProjectRequestDto requestDto) {
        if (requestDto == null) return null;
        Project project = new Project();
        project.setName(requestDto.getName());
        project.setDescription(requestDto.getDescription());
        project.setStatus(requestDto.getStatus());
        project.setStartDate(requestDto.getStartDate());
        project.setEndDate(requestDto.getEndDate());
        return project;
    }

    public Department mapDepartmentToEntity(DepartmentRequestDto requestDto) {
        if (requestDto == null) return null;
        Department department = new Department();
        department.setName(requestDto.getName());
        department.setDescription(requestDto.getDescription());
        department.setLocation(requestDto.getLocation());
        return department;
    }

    public User mapUserToEntity(UserRequestDto requestDto) {
        if (requestDto == null) return null;
        User user = new User();
        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user.setEmail(requestDto.getEmail());
        user.setPhone(requestDto.getPhone());
        user.setRole(requestDto.getRole());
        user.setPayRoll(requestDto.getPayRoll());
        Department dept = departmentRepository.findById(requestDto.getDepartmentId()).orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + requestDto.getDepartmentId()));
        user.setDepartment(dept);
        user.setProjects(requestDto.getProjects());
        user.setPayRoll(requestDto.getPayRoll());
        return user;
    }
}

