package com.example.user_management_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.user_management_service.dto.ProjectRequestDto;
import com.example.user_management_service.dto.ProjectResponseDto;
import com.example.user_management_service.service.ProjectService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/create")
    public ResponseEntity<ProjectResponseDto> createProject(@Valid @RequestBody ProjectRequestDto requestDto) {
        ProjectResponseDto responseDto = projectService.createProject(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> getProjectById(@PathVariable Long id) {
        ProjectResponseDto responseDto = projectService.getProjectById(id);
        return ResponseEntity.ok(responseDto);
    }
    
    @GetMapping("/allprojects")
    public ResponseEntity<List<ProjectResponseDto>> getAllProjects() {
        List<ProjectResponseDto> responseDtos = projectService.getAllProjects();
        return ResponseEntity.ok(responseDtos);
    }

    @PutMapping("/updateproject/{id}")
    public ResponseEntity<ProjectResponseDto> updateProject(@PathVariable Long id, @Valid @RequestBody ProjectRequestDto requestDto) {
        ProjectResponseDto responseDto = projectService.updateProject(id, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/deleteproject/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
    
}
