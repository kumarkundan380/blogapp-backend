package com.blogapp.enums;

public enum Gender {
    MALE ("MALE"),

    /* There was an error while processing the request */
    FEMALE ("FEMALE"),

    /* Failed to process the request */
    OTHERS ("OTHERS");

    private final String value;

    public String getValue() {
        return value;
    }
    Gender(String value){
        this.value = value;
    }
}
