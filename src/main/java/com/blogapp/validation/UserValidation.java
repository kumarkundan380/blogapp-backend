package com.blogapp.validation;

import com.blogapp.dto.UserDTO;
import com.blogapp.exception.BlogAppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Slf4j
public class UserValidation {

    public static void validateUser(UserDTO user){
        log.info("validateUser method invoking");
        if(!StringUtils.hasText(user.getFirstName())){
            log.error("First Name is empty");
            throw new BlogAppException("First Name cannot be empty");
        } else if (!StringUtils.hasText(user.getUserName())) {
            log.error("Username is empty");
            throw new BlogAppException("Username cannot be empty");
        } else if(ObjectUtils.isEmpty(user.getGender())){
            log.error("Gender is empty");
            throw new BlogAppException("Gender cannot be empty");
        }
        log.info("Removing extra spaces if any mandatory fields contains");
        user.setUserName(user.getUserName().trim().toLowerCase());
        user.setFirstName(user.getFirstName().trim());
        log.info("validateUser method called");
    }


}
