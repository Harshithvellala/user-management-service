package com.example.user_management_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.user_management_service.dto.ProjectRequestDto;
import com.example.user_management_service.dto.ProjectResponseDto;
import com.example.user_management_service.exception.ResourceNotFoundException;
import com.example.user_management_service.model.Project;
import com.example.user_management_service.repository.ProjectRepository;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public ProjectResponseDto createProject(ProjectRequestDto projectRequestDto) {
        if(projectRequestDto.getStartDate() != null && projectRequestDto.getEndDate() != null && projectRequestDto.getStartDate().isAfter(projectRequestDto.getEndDate())) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        if(projectRepository.findByName(projectRequestDto.getName()).isPresent()) {
            throw new IllegalArgumentException("Project with the same name already exists");
        }
        Project project = mapToEntity(projectRequestDto);
        Project savedProject = projectRepository.save(project);
        return mapToResponseDto(savedProject);
    }

    public List<ProjectResponseDto> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream().map(this::mapToResponseDto).toList();
    }

    public ProjectResponseDto getProjectById(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
        return mapToResponseDto(project);
    }

    public ProjectResponseDto updateProject(Long id, ProjectRequestDto projectRequestDto) {
        Project existingProject = projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
        existingProject.setName(projectRequestDto.getName());
        existingProject.setDescription(projectRequestDto.getDescription());
        existingProject.setStatus(projectRequestDto.getStatus());
        existingProject.setStartDate(projectRequestDto.getStartDate());
        existingProject.setEndDate(projectRequestDto.getEndDate());

        Project updatedProject = projectRepository.save(existingProject);
        return mapToResponseDto(updatedProject);
    }


    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
        projectRepository.delete(project);
    }

    private Project mapToEntity(ProjectRequestDto projectRequestDto) {
        Project project = new Project();
        project.setName(projectRequestDto.getName());
        project.setDescription(projectRequestDto.getDescription());
        project.setStatus(projectRequestDto.getStatus());
        project.setStartDate(projectRequestDto.getStartDate());
        project.setEndDate(projectRequestDto.getEndDate());
        return project;
    }

    private ProjectResponseDto mapToResponseDto(Project project) {
        ProjectResponseDto responseDto = new ProjectResponseDto();
        responseDto.setId(project.getId());
        responseDto.setName(project.getName());
        responseDto.setDescription(project.getDescription());
        responseDto.setStatus(project.getStatus());
        responseDto.setStartDate(project.getStartDate());
        responseDto.setEndDate(project.getEndDate());
        return responseDto;
    }
}
