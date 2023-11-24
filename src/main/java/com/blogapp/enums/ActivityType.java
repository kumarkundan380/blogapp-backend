package com.blogapp.enums;

public enum ActivityType {
    LIKE ("LIKE"),
    DISLIKE ("DISLIKE");
    private final String value;

    public String getValue() {
        return value;
    }
    ActivityType(String value){
        this.value = value;
    }
}
