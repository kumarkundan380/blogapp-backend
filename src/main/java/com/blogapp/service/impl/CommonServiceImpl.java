package com.blogapp.service.impl;

import com.blogapp.dto.PostDTO;
import com.blogapp.dto.RoleDTO;
import com.blogapp.dto.UserDTO;
import com.blogapp.model.Post;
import com.blogapp.model.User;
import com.blogapp.service.CommonService;
import com.blogapp.util.AuthorityUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommonServiceImpl implements CommonService {

    private final ModelMapper modelMapper;

    @Autowired
    public CommonServiceImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    @Override
    public UserDTO convertUserToUserDTO(User user) {
        log.info("convertUserToUserDTO method invoking");
        UserDTO userDTO = modelMapper.map(user,UserDTO.class);
        if(AuthorityUtil.isAdminRole()){
            userDTO.setUserStatus(user.getStatus());
            userDTO.setIsVerified(user.getIsUserVerified());
            userDTO.setIsAccountExpired(!user.getAccountNonExpired());
            userDTO.setIsAccountLocked(!user.getAccountNonLocked());
            userDTO.setIsCredentialsExpired(!user.getCredentialsNonExpired());
        }
        Set<RoleDTO> roleDTOS = user.getRoles().stream().map(r -> modelMapper.map(r, RoleDTO.class)).collect(Collectors.toSet());
        userDTO.setRoles(roleDTOS);
        log.info("convertUserToUserDTO method called");
        return userDTO;
    }

    @Override
    public PostDTO convertPostToPostDTO(Post post) {
        log.info("convertPostToPostDTO method invoking");
        PostDTO postDTO = modelMapper.map(post,PostDTO.class);
        User user = post.getUser();
        UserDTO userDTO = convertUserToUserDTO(user);
        postDTO.setUser(userDTO);
        if(AuthorityUtil.isAdminRole()){
            postDTO.setStatus(post.getPostStatus());
        }
        log.info("convertUserToUserDTO method called");
        return postDTO;
    }
}
