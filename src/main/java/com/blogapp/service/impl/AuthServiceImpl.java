package com.blogapp.service.impl;

import com.blogapp.exception.BlogAppException;
import com.blogapp.repository.UserRepository;
import com.blogapp.request.LoginRequest;
import com.blogapp.response.LoginResponse;
import com.blogapp.security.JwtUtil;
import com.blogapp.service.AuthService;
import com.blogapp.service.CommonService;
import com.blogapp.validation.LoginRequestValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    private final CommonService commonService;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, JwtUtil jwtUtil, AuthenticationManager authenticationManager, CommonService commonService) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.commonService = commonService;
    }
    @Override
    public LoginResponse loginUser(LoginRequest loginRequest) {
        log.info("loginUser method invoking");
        LoginRequestValidation.validateLoginRequest(loginRequest);
        if(userRepository.isUserExist(loginRequest.getUserName()) && userRepository.isDeletedUser(loginRequest.getUserName())){
            throw new BlogAppException("Either User does not exist or User is deleted!!");
        }
        authenticate(loginRequest);
        String token = jwtUtil.generateToken(loginRequest.getUserName());
        return LoginResponse.builder()
                .token(token)
                .user( commonService.convertUserToUserDTO(userRepository.getUserByUsername(loginRequest.getUserName())))
                .build();

    }

    private void authenticate(LoginRequest loginRequest) {
        log.info("authenticate method invoking");
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUserName(),
                            loginRequest.getPassword()));
        } catch(BadCredentialsException e) {
            throw new BlogAppException("Enter Valid Username and Password");
        }
    }
}
