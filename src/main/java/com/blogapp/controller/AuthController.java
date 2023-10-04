package com.blogapp.controller;

import com.blogapp.enums.ResponseStatus;
import com.blogapp.request.LoginRequest;
import com.blogapp.response.BlogAppResponse;
import com.blogapp.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.blogapp.constant.BlogAppConstant.BASE_PATH_AUTH;
import static com.blogapp.constant.BlogAppConstant.LOGIN;

@RestController
@RequestMapping(BASE_PATH_AUTH)
public class AuthController {

	private final AuthService authService;

	@Autowired
	public AuthController(AuthService authService) {
		this.authService = authService;
	}
	
	@PostMapping(LOGIN)
	public ResponseEntity<BlogAppResponse<?>> login(@Valid @RequestBody LoginRequest loginRequest){
		return new ResponseEntity<>(BlogAppResponse.builder()
				.status(ResponseStatus.SUCCESS)
				.message("User Logged in successfully")
				.body(authService.loginUser(loginRequest))
				.build(),
				HttpStatus.OK); 	
	}

}
