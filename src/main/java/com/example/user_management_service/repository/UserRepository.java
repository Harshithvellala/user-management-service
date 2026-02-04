package com.example.user_management_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.user_management_service.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);

    List<User> findByDepartmentId(long departmentId);
}
