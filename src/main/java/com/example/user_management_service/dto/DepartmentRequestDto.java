package com.example.user_management_service.dto;

import jakarta.validation.constraints.NotBlank;

public class DepartmentRequestDto {
    @NotBlank(message = "Department name is required")
    private String name;
    private String description;
    @NotBlank(message = "Location is required")
    private String location;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
}
