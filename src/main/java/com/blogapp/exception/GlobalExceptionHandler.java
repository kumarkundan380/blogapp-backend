package com.blogapp.exception;

import com.blogapp.enums.ResponseStatus;
import com.blogapp.response.BlogAppErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<BlogAppErrorResponse<?>> handleResourceNotFoundException(ResourceNotFoundException e){
		return new ResponseEntity<>(BlogAppErrorResponse.builder()
				.status(ResponseStatus.FAILURE)
				.errorMessage(e.getMessage())
				.build(),HttpStatus.NOT_FOUND);
		
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<BlogAppErrorResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
		Map<String,String> response = new HashMap<>();
		e.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError)error).getField();
			String message = error.getDefaultMessage();
			response.put(fieldName, message);
		});
		
		return new ResponseEntity<>(BlogAppErrorResponse.builder()
				.status(ResponseStatus.FAILURE)
				.errorMessage(response)
				.build(),HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(BlogAppException.class)
	public ResponseEntity<BlogAppErrorResponse<?>> handleBlogAppException(BlogAppException e){
		return new ResponseEntity<>(BlogAppErrorResponse.builder()
				.status(ResponseStatus.FAILURE)
				.errorMessage(e.getMessage())
				.build(),HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<BlogAppErrorResponse<?>> handleRemainingException(Exception e){
		return new ResponseEntity<>(BlogAppErrorResponse.builder()
				.status(ResponseStatus.ERROR)
				.errorMessage("Oops! Something went wrong. Please try again"+e.getMessage())
				.build(),HttpStatus.INTERNAL_SERVER_ERROR);

	}
	
}
