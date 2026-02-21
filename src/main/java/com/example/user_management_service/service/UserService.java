package com.example.user_management_service.service;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.user_management_service.dto.DepartmentResponseDto;
import com.example.user_management_service.dto.UserRequestDto;
import com.example.user_management_service.dto.UserResponseDto;
import com.example.user_management_service.exception.ResourceNotFoundException;
import com.example.user_management_service.model.Department;
import com.example.user_management_service.model.Project;
import com.example.user_management_service.model.Role;
import com.example.user_management_service.model.User;
import com.example.user_management_service.repository.DepartmentRepository;
import com.example.user_management_service.repository.ProjectRepository;
import com.example.user_management_service.repository.UserRepository;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final DepartmentService departmentService;
    private final ProjectService projectService;
    private final ProjectRepository projectRepository;

    public UserService(UserRepository userRepository, DepartmentRepository departmentRepository, DepartmentService departmentService, ProjectService projectService, ProjectRepository projectRepository) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.departmentService = departmentService;
        this.projectService = projectService;
        this.projectRepository = projectRepository;
    }

    public UserResponseDto registerUser(UserRequestDto newUserRequestDto) {
        validatePayrollAcccess(newUserRequestDto);
        User newUser = mapToEntity(newUserRequestDto);
        User savedUser = userRepository.save(newUser);
        return mapToResponse(savedUser);
    }
    
    public List<UserResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::mapToResponse).toList();
    }

    public UserResponseDto getUserById(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return mapToResponse(user);
    }

    public User getUserByEmail(String email) {
        // return userRepository.findByEmail(email); 
        return null;  
    }

    public UserResponseDto updateUser(long id, UserRequestDto updatedUserRequestDto){
        User existingUser = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id)); 
        validatePayrollAcccess(updatedUserRequestDto);
        existingUser.setFirstName(updatedUserRequestDto.getFirstName());
        existingUser.setLastName(updatedUserRequestDto.getLastName());
        existingUser.setEmail(updatedUserRequestDto.getEmail());
        existingUser.setPhone(updatedUserRequestDto.getPhone());
        existingUser.setRole(updatedUserRequestDto.getRole());
        Department dept = departmentRepository.findById(updatedUserRequestDto.getDepartmentId()).orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + updatedUserRequestDto.getDepartmentId()));
        existingUser.setDepartment(dept);
        User updatedUser = userRepository.save(existingUser);
        return mapToResponse(updatedUser);
    }

    public void deleteUser(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
    }

    public List<UserResponseDto> getUsersByDepartmentId(long departmentId) {
        List<User> users = userRepository.findByDepartmentId(departmentId);
        if(users == null || users.isEmpty()) {
            throw new ResourceNotFoundException("No users found for department with id: " + departmentId);
        }
        return users.stream().map(this::mapToResponse).toList();
    }

    public DepartmentResponseDto getDepartmentByUserId(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Department department = departmentRepository.findById(user.getDepartment().getId()).orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + user.getDepartment().getId()));
        return departmentService.mapToResponse(department);
    }

    public UserResponseDto updateUserDepartment(Long userId, Long departmentId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Department department = departmentRepository.findById(departmentId).orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + departmentId));
        user.setDepartment(department);
        User updatedUser = userRepository.save(user);
        return mapToResponse(updatedUser);
    }

    private void validatePayrollAcccess(UserRequestDto userRequestDto) {
        if (userRequestDto.getRole() != null && userRequestDto.getRole() == Role.USER
                && userRequestDto.getPayRoll() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Users with role USER cannot have payroll information.");
        }
    }

    public List<UserResponseDto> getUsersByProjectId(Long projectId) {
        Set<User> users = projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId)).getUsers();
        if(users == null || users.isEmpty()) {
            throw new ResourceNotFoundException("No users found for project with id: " + projectId);
        }
        return users.stream().map(this::mapToResponse).toList();
    }

    public UserResponseDto assignUserToProject(Long userId, Long projectId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));
        user.getProjects().add(project);
        User updatedUser = userRepository.save(user);
        return mapToResponse(updatedUser);
    }

    private User mapToEntity(UserRequestDto userRequestDto) {
        User user = new User();
        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getLastName());
        user.setEmail(userRequestDto.getEmail());
        user.setPhone(userRequestDto.getPhone());
        user.setRole(userRequestDto.getRole());
        Department dept = departmentRepository.findById(userRequestDto.getDepartmentId()).orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + userRequestDto.getDepartmentId()));
        user.setDepartment(dept);
        user.setProjects(userRequestDto.getProjects());
        user.setPayRoll(userRequestDto.getPayRoll());
        return user;
    }

    private UserResponseDto mapToResponse(User user){
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());
        dto.setDepartmentId(user.getDepartment().getId());
        dto.setDepartmentName(user.getDepartment().getName());
        dto.setProjects(user.getProjects());
        dto.setPayRoll(user.getPayRoll());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }
}
