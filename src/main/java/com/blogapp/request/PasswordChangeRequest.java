package com.blogapp.request;

import java.io.Serial;
import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeRequest {

	@NotBlank(message = "Old Password must not be null or empty")
	private String oldPassword;
	
	@NotBlank(message = "New Password must not be null or empty")
	private String newPassword;
}
