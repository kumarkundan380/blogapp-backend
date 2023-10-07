package com.blogapp.enums;

public enum UserRole {
    USER ("USER"),
    ADMIN ("ADMIN"),
    SUPER_ADMIN("SUPER ADMIN"),
    MODERATE("MODERATE");

    private final String value;

    UserRole(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
