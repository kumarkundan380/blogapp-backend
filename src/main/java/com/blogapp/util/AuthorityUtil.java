package com.blogapp.util;

import com.blogapp.enums.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthorityUtil {

    public static Boolean isAdminRole(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().toString().contains(UserRole.ADMIN.getValue());
    }

    public static Boolean isSameUser(String userName){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userName.equals(authentication.getPrincipal());
    }
}
