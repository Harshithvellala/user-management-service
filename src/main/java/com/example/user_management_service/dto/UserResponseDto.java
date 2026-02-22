package com.example.user_management_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import com.example.user_management_service.model.Role;

public class UserResponseDto {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Role role;
    private Long departmentId;
    private String departmentName;
    private Set<ProjectResponseDto> projects;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BigDecimal payRoll;


    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }
    public Long getDepartmentId() {
        return departmentId;
    }
    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
    public String getDepartmentName() {
        return departmentName;
    }
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
    public Set<ProjectResponseDto> getProjects() {
        return projects;
    }
    public void setProjects(Set<ProjectResponseDto> projects) {
        this.projects = projects;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public BigDecimal getPayRoll() {
        return payRoll;
    }
    public void setPayRoll(BigDecimal payRoll) {
        this.payRoll = payRoll;
    }
}
