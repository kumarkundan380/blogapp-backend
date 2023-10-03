package com.blogapp.enums;

public enum ResponseStatus {

    SUCCESS ("SUCCESS"),

    /* There was an error while processing the request */
    ERROR ("ERROR"),

    /* Failed to process the request */
    FAILURE ("FAILURE");

    private final String value;

    public String getValue() {
        return value;
    }
    ResponseStatus(String value){
        this.value = value;
    }
}
