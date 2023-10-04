package com.blogapp.service;

import com.blogapp.request.LoginRequest;
import com.blogapp.response.LoginResponse;

public interface AuthService {
    LoginResponse loginUser(LoginRequest loginRequest);
}
