package com.blogapp.exception;

import java.io.Serial;

public class BlogAppException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 1L;
	
	public BlogAppException(String message){
		super(message);
	}

}
