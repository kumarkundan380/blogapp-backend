package com.blogapp.enums;

public enum EntityType {
    POST("POST"),
    COMMENT("COMENT");
    private final String value;
    public String getValue() {
        return value;
    }
    EntityType(String value){
        this.value = value;
    }
}
