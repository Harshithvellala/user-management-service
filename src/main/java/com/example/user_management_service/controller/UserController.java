package com.example.user_management_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.user_management_service.dto.DepartmentResponseDto;
import com.example.user_management_service.dto.UserRequestDto;
import com.example.user_management_service.dto.UserResponseDto;
import com.example.user_management_service.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto response = userService.registerUser(userRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable("id") long id) {
        UserResponseDto userResponseDto = userService.getUserById(id);
        return ResponseEntity.ok(userResponseDto);
    }
    
    @PutMapping("/update/{id}")
    public ResponseEntity<UserResponseDto> updateUserById(@PathVariable long id, @Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto updatedUser = userService.updateUser(id, userRequestDto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/allusers")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok().body(userService.getAllUsers());
    }
    
    @GetMapping("/department/{id}/users")
    public ResponseEntity<List<UserResponseDto>> getUsersByDepartmentId(@PathVariable("id") long departmentId) {
        return ResponseEntity.ok(userService.getUsersByDepartmentId(departmentId));
    }
    
    @GetMapping("/{id}/department")
    public ResponseEntity<DepartmentResponseDto> getDepartmentByUserId(@PathVariable("id") long userId) {
        DepartmentResponseDto departmentResponseDto = userService.getDepartmentByUserId(userId);
        return ResponseEntity.ok(departmentResponseDto);
    }
    
    @PutMapping("/{id}/department/{deptId}")
    public ResponseEntity<UserResponseDto> updateUserDepartment(@PathVariable long id, @PathVariable long deptId) {
        UserResponseDto updatedUser = userService.updateUserDepartment(id, deptId);
        return ResponseEntity.ok(updatedUser);
    }
    
}
