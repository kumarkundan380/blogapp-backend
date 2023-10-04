package com.blogapp.service;

import com.blogapp.dto.UserDTO;
import com.blogapp.model.User;

public interface CommonService {
    UserDTO convertUserToUserDTO(User user);
}
