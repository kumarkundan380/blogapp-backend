package com.blogapp.enums;

public enum PostStatus {

    APPROVED("APPROVED"),
    PENDING("PENDING"),
    DELETED("DELETED");

    private final String value;

    public String getValue() {
        return value;
    }
    PostStatus(String value){
        this.value = value;
    }
}
