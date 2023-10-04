package com.blogapp.validation;

import com.blogapp.exception.BlogAppException;
import com.blogapp.request.PasswordChangeRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
public class PasswordChangedRequestValidation {

    public static void validatePasswordChangedRequest(PasswordChangeRequest passwordChangeRequest){
        log.info("validatePasswordChangedRequest method invoking");
        if(!StringUtils.hasText(passwordChangeRequest.getOldPassword())){
            throw new BlogAppException("Old Password cannot be empty");
        }
        if(!StringUtils.hasText(passwordChangeRequest.getNewPassword())){
            throw new BlogAppException("New Password cannot be empty");
        }
        passwordChangeRequest.setOldPassword(passwordChangeRequest.getOldPassword().trim());
        passwordChangeRequest.setNewPassword(passwordChangeRequest.getNewPassword().trim());
    }
}
