package com.example.user_management_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.user_management_service.dto.DepartmentRequestDto;
import com.example.user_management_service.dto.DepartmentResponseDto;
import com.example.user_management_service.exception.ResourceNotFoundException;
import com.example.user_management_service.model.Department;
import com.example.user_management_service.repository.DepartmentRepository;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public DepartmentResponseDto createDepartment(DepartmentRequestDto departmentRequestDto) {
        Department department = mapToEntity(departmentRequestDto);
        if(departmentRepository.existsByName(department.getName())) {
            throw new IllegalStateException("Department with name " + department.getName() + " already exists.");
        }
        return mapToResponse(departmentRepository.save(department));
    }

    public DepartmentResponseDto getDepartmentById(long id) {
        Department response = departmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        return mapToResponse(response);
    }
    
    public DepartmentResponseDto getDepartmentByName(String name) {
        Department response = departmentRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Department not found with name: " + name));
        return mapToResponse(response);
    }

    public List<DepartmentResponseDto> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        return departments.stream().map(this::mapToResponse).toList();
    }

    public DepartmentResponseDto updateDepartment(long id, DepartmentRequestDto departmentRequestDto) {
        Department existingDepartment = departmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id)); 
        existingDepartment.setName(departmentRequestDto.getName());
        existingDepartment.setDescription(departmentRequestDto.getDescription());
        existingDepartment.setLocation(departmentRequestDto.getLocation());
        Department updatedDepartment = departmentRepository.save(existingDepartment);
        return mapToResponse(updatedDepartment);
    }

    public void deleteDepartment(long id) {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        departmentRepository.delete(department);
    }

    private Department mapToEntity(DepartmentRequestDto departmentRequestDto) {
        Department department = new Department();
        department.setName(departmentRequestDto.getName());
        department.setDescription(departmentRequestDto.getDescription());
        department.setLocation(departmentRequestDto.getLocation());
        return department;
    }

    DepartmentResponseDto mapToResponse(Department department) {
        DepartmentResponseDto dto = new DepartmentResponseDto();
        dto.setId(department.getId());
        dto.setName(department.getName());
        dto.setDescription(department.getDescription());
        dto.setLocation(department.getLocation());
        return dto;
    }
}
