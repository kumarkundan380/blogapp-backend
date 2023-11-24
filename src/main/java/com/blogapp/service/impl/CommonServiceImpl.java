package com.blogapp.service.impl;

import com.blogapp.dto.ActivityDTO;
import com.blogapp.dto.CommentDTO;
import com.blogapp.dto.PostDTO;
import com.blogapp.dto.RoleDTO;
import com.blogapp.dto.UserDTO;
import com.blogapp.model.Activity;
import com.blogapp.model.Comment;
import com.blogapp.model.Post;
import com.blogapp.model.User;
import com.blogapp.service.CommonService;
import com.blogapp.util.AuthorityUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
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
        List<CommentDTO> commentsDTOS = new ArrayList<>();
        if(!CollectionUtils.isEmpty(post.getComments())) {
            commentsDTOS = post.getComments().stream().map(this::convertCommentToCommentDTO).collect(Collectors.toList());
            commentsDTOS = commentsDTOS.stream()
                    .sorted(Comparator.comparing(CommentDTO::getCreatedAt))
                    .collect(Collectors.toList());
        }
        postDTO.setComments(commentsDTOS);
        Set<ActivityDTO> activityDTOS = new HashSet<>();
        if(!CollectionUtils.isEmpty(post.getActivities())){
            activityDTOS = post.getActivities().stream().map(this::convertActivityToActivityDTO).collect(Collectors.toSet());
        }
        postDTO.setActivities(activityDTOS);
        if(AuthorityUtil.isAdminRole()){
            postDTO.setStatus(post.getPostStatus());
        }
        log.info("convertUserToUserDTO method called");
        return postDTO;
    }

    @Override
    public CommentDTO convertCommentToCommentDTO(Comment comment) {
        log.info("convertCommentToCommentDTO method invoking");
        CommentDTO commentDTO = modelMapper.map(comment,CommentDTO.class);
        UserDTO userDTO = convertUserToUserDTO(comment.getUser());
        commentDTO.setUser(userDTO);
        Set<ActivityDTO> activityDTOS = new HashSet<>();
        if(!CollectionUtils.isEmpty(comment.getActivities())){
            activityDTOS = comment.getActivities().stream().map(this::convertActivityToActivityDTO).collect(Collectors.toSet());
        }
        commentDTO.setActivities(activityDTOS);
        return commentDTO;
    }

    @Override
    public ActivityDTO convertActivityToActivityDTO(Activity activity) {
        log.info("convertCommentToCommentDTO method invoking");
        UserDTO user = convertUserToUserDTO(activity.getUser());
        ActivityDTO activityDTO = modelMapper.map(activity,ActivityDTO.class);
        activityDTO.setUser(user);
        return activityDTO;
    }
}
