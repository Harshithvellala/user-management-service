package com.example.user_management_service.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.user_management_service.dto.ProjectRequestDto;
import com.example.user_management_service.dto.ProjectResponseDto;
import com.example.user_management_service.dto.UserResponseDto;
import com.example.user_management_service.exception.ResourceNotFoundException;
import com.example.user_management_service.model.Project;
import com.example.user_management_service.model.User;
import com.example.user_management_service.repository.ProjectRepository;
import com.example.user_management_service.util.DtoMapper;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final DtoMapper dtoMapper;

    public ProjectService(ProjectRepository projectRepository, DtoMapper dtoMapper) {
        this.projectRepository = projectRepository;
        this.dtoMapper = dtoMapper;
    }

    public ProjectResponseDto createProject(ProjectRequestDto projectRequestDto) {
        if(projectRequestDto.getStartDate() != null && projectRequestDto.getEndDate() != null && projectRequestDto.getStartDate().isAfter(projectRequestDto.getEndDate())) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        if(projectRepository.findByName(projectRequestDto.getName()).isPresent()) {
            throw new IllegalArgumentException("Project with the same name already exists");
        }
        Project project = dtoMapper.mapProjectToEntity(projectRequestDto);
        Project savedProject = projectRepository.save(project);
        return dtoMapper.mapProjectToResponse(savedProject);
    }

    public List<ProjectResponseDto> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream().map(dtoMapper::mapProjectToResponse).toList();
    }

    public ProjectResponseDto getProjectById(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
        return dtoMapper.mapProjectToResponse(project);
    }

    public ProjectResponseDto updateProject(Long id, ProjectRequestDto projectRequestDto) {
        Project existingProject = projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
        existingProject.setName(projectRequestDto.getName());
        existingProject.setDescription(projectRequestDto.getDescription());
        existingProject.setStatus(projectRequestDto.getStatus());
        existingProject.setStartDate(projectRequestDto.getStartDate());
        existingProject.setEndDate(projectRequestDto.getEndDate());

        Project updatedProject = projectRepository.save(existingProject);
        return dtoMapper.mapProjectToResponse(updatedProject);
    }


    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
        projectRepository.delete(project);
    }

    public List<UserResponseDto> getProjectUsers(Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));
        Set<User> users = project.getUsers();
        if(users == null || users.isEmpty()) {
            throw new ResourceNotFoundException("No users found for project with id: " + projectId);
        }
        return users.stream().map(dtoMapper::mapUserToResponse).toList();
    }

    
}
