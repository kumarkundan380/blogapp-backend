package com.blogapp.exception;

import java.io.Serial;

public class ResourceNotFoundException extends RuntimeException {
	
	@Serial
	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(String resourceName,String fieldName,Integer fieldvalue) {
		super(String.format("%s Not Found with %s : %d", resourceName, fieldName, fieldvalue));
	}

}
