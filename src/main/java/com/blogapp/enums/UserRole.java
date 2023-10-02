package com.blogapp.enums;

public enum UserRole {
    USER ("USER"),
    ADMIN ("ADMIN");

    private final String value;

    public String getValue() {
        return value;
    }
    UserRole(String value){
        this.value = value;
    }
}
