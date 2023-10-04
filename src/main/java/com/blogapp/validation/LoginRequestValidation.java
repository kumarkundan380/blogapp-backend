package com.blogapp.validation;

import com.blogapp.exception.BlogAppException;
import com.blogapp.request.LoginRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
@Slf4j
public class LoginRequestValidation {

    public static void validateLoginRequest(LoginRequest loginRequest){
        log.info("validateLoginRequest method invoking");
        if(!StringUtils.hasText(loginRequest.getUserName())){
            throw new BlogAppException("Username cannot be empty");
        }
        if(!StringUtils.hasText(loginRequest.getPassword())){
            throw new BlogAppException("Password cannot be empty");
        }
        loginRequest.setUserName(loginRequest.getUserName().trim().toLowerCase());
        loginRequest.setPassword(loginRequest.getPassword().trim());
    }
}
