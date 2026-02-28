package com.example.user_management_service.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.example.user_management_service.dto.ProjectRequestDto;
import com.example.user_management_service.dto.ProjectResponseDto;
import com.example.user_management_service.exception.ResourceNotFoundException;
import com.example.user_management_service.model.Project;
import com.example.user_management_service.model.Status;
import com.example.user_management_service.repository.ProjectRepository;
import com.example.user_management_service.util.DtoMapper;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private DtoMapper dtoMapper;
    @InjectMocks
    private ProjectService projectService;

    @Test
    void testCreateProject() {
        // step-1 create request mocking users request
        ProjectRequestDto requestDto = new ProjectRequestDto();
        requestDto.setName("Project Alpha");
        requestDto.setDescription("Description of Project Alpha");
        requestDto.setStatus(Status.READY);
        // step-2 map request to entity
        Project project = new Project();
        project.setId(1L);
        project.setName(requestDto.getName());
        project.setDescription(requestDto.getDescription());
        project.setStatus(requestDto.getStatus());
        // step-3 define saved entity
        Project savedProject = new Project();
        savedProject.setId(project.getId());
        savedProject.setName(project.getName());
        savedProject.setDescription(project.getDescription());
        savedProject.setStatus(project.getStatus());
        // step-4 map entity to resposne
        ProjectResponseDto responseDto = new ProjectResponseDto();
        responseDto.setId(savedProject.getId());
        responseDto.setName(savedProject.getName());
        responseDto.setDescription(savedProject.getDescription());
        responseDto.setStatus(savedProject.getStatus());
        // step-5, all objects are ready, mock behaviour
        // define mapping request to entity
        when(dtoMapper.mapProjectToEntity(requestDto)).thenReturn(project);
        // define saving entity to repository
        when(projectRepository.save(any(Project.class))).thenReturn(savedProject);
        // define mapping entity to response
        when(dtoMapper.mapProjectToResponse(savedProject)).thenReturn(responseDto);
        // step-6 fake call service method
        ProjectResponseDto result = projectService.createProject(requestDto);
        // step-7 verify results
        assertNotNull(result);
        assertEquals("Project Alpha", result.getName());
        assertEquals("Description of Project Alpha", result.getDescription());
        assertEquals(Status.READY, result.getStatus());
        // step-8 verify interactions
        verify(projectRepository).save(project);
    }

    @Test
    void testGetProjectById(){
        //mock the entity to return
        Project project = new Project();
        project.setId(1L);
        project.setName("test project");
        project.setDescription("test description");
        project.setStatus(Status.READY);
        //mock the response dto to return
        ProjectResponseDto projectResponseDto = new ProjectResponseDto();
        projectResponseDto.setId(1L);
        projectResponseDto.setName("test project");
        projectResponseDto.setStatus(Status.READY);
        projectResponseDto.setDescription("test description");
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(dtoMapper.mapProjectToResponse(project)).thenReturn(projectResponseDto);
        // fake call service method
        ProjectResponseDto result = projectService.getProjectById(1L);
        // verify results
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("test project", result.getName());
        assertEquals("test description", result.getDescription());
        assertEquals(Status.READY, result.getStatus());
        // verify interactions
        verify(projectRepository).findById(1L);
    }

    @Test
    void testGetProjectById_NotFound() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,() -> projectService.getProjectById(1L));
        verify(projectRepository).findById(1L);
        verify(dtoMapper, never()).mapProjectToResponse(any());
    }

    @Test
    void testUpdateProject() {
        //mock the existing entity
        Project existingProject = new Project();
        existingProject.setId(1L);
        existingProject.setName("test project");
        existingProject.setDescription("test description");
        existingProject.setStatus(Status.READY);
        //mock the request dto
        ProjectRequestDto requestDto = new ProjectRequestDto();
        requestDto.setName("updated project");
        requestDto.setDescription("updated description");
        requestDto.setStatus(Status.IN_PROGRESS);
        //mock the updated entity
        Project updatedProject = new Project();
        updatedProject.setId(1L);
        updatedProject.setName(requestDto.getName());
        updatedProject.setDescription(requestDto.getDescription());
        updatedProject.setStatus(requestDto.getStatus());
        //mock the response dto
        ProjectResponseDto responseDto = new ProjectResponseDto();
        responseDto.setId(updatedProject.getId());
        responseDto.setName(updatedProject.getName());
        responseDto.setDescription(updatedProject.getDescription());
        responseDto.setStatus(updatedProject.getStatus());
        //define mock behaviour
        when(projectRepository.findById(1L)).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(any(Project.class))).thenReturn(updatedProject);
        when(dtoMapper.mapProjectToResponse(updatedProject)).thenReturn(responseDto);
        // fake call service method
        ProjectResponseDto result = projectService.updateProject(1L, requestDto);
        // verify results
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("updated project", result.getName());
        assertEquals("updated description", result.getDescription());
        assertEquals(Status.IN_PROGRESS, result.getStatus());
        // verify interactions
        verify(projectRepository).findById(1L);
        verify(projectRepository).save(existingProject);
    }

    @Test
    void testDeleteProject() {
        //mock the existing entity
        Project existingProject = new Project();
        existingProject.setId(1L);
        existingProject.setName("test project");
        existingProject.setDescription("test description");
        existingProject.setStatus(Status.READY);
        //define mock behaviour
        when(projectRepository.findById(1L)).thenReturn(Optional.of(existingProject));
        // fake call service method
        projectService.deleteProject(1L);
        // verify interactions
        verify(projectRepository).findById(1L);
        verify(projectRepository).delete(existingProject);
    }
}
