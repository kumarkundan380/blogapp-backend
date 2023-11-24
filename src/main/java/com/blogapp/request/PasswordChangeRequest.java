package com.blogapp.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeRequest {

	@NotBlank(message = "Password must not be null or empty")
	private String password;
	
	@NotBlank(message = "Confirm Password must not be null or empty")
	private String confirmPassword;

	@NotBlank(message = "Token must not be null or empty")
	private String token;
}
