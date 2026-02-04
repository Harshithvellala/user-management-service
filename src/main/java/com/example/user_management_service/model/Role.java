package com.example.user_management_service.model;

public enum Role {
    ADMIN,
    USER,
    MANAGER;

    public static boolean isAdmin(Role role){
        return role == ADMIN;
    }
    public static boolean isManager(Role role){
        return role == MANAGER;
    }
    public static boolean isUser(Role role){
        return role == USER;
    }
}