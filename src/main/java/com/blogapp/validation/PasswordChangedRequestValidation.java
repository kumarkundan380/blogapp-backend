package com.blogapp.validation;

import com.blogapp.exception.BlogAppException;
import com.blogapp.request.PasswordChangeRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
public class PasswordChangedRequestValidation {

    public static void validatePasswordChangedRequest(PasswordChangeRequest passwordChangeRequest){
        log.info("validatePasswordChangedRequest method invoking");
        if(!StringUtils.hasText(passwordChangeRequest.getPassword())){
            throw new BlogAppException("Password cannot be empty");
        }
        if(!StringUtils.hasText(passwordChangeRequest.getConfirmPassword())){
            throw new BlogAppException("Confirm Password cannot be empty");
        }
        if(!StringUtils.hasText(passwordChangeRequest.getToken())){
            throw new BlogAppException("Token cannot be empty");
        }
        if(!passwordChangeRequest.getPassword().equals(passwordChangeRequest.getConfirmPassword())){
            throw new BlogAppException("Confirm Password not matched with Password");
        }
        passwordChangeRequest.setPassword(passwordChangeRequest.getPassword().trim());
        passwordChangeRequest.setConfirmPassword(passwordChangeRequest.getConfirmPassword().trim());
    }
}
