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
public class PasswordChangeRequest implements Serializable {
	
	@Serial
	private static final long serialVersionUID = 1L;
	
	@NotBlank(message = "Old Password must not be null or empty")
	private String oldPassword;
	
	@NotBlank(message = "New Password must not be null or empty")
	private String newPassword;
}
