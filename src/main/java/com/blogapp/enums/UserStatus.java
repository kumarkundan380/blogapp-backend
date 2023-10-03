package com.blogapp.enums;

public enum UserStatus {

	ACTIVE ("ACTIVE"),
	DELETED ("DELETED");

	private final String value;

	public String getValue() {
		return value;
	}
	UserStatus(String value){
		this.value = value;
	}

}
