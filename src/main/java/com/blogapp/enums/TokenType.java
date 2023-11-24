package com.blogapp.enums;

public enum TokenType {
    EMAIL_VERIFICATION("EMAIL_VERIFICATION"),
    FORGOT_PASSWORD("FORGOT_PASSWORD");
    private final String value;
    public String getValue() {
        return value;
    }
    TokenType(String value){
        this.value = value;
    }
}
