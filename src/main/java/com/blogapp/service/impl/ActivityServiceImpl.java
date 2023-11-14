package com.blogapp.service.impl;

import com.blogapp.dto.ActivityDTO;
import com.blogapp.dto.UserDTO;
import com.blogapp.exception.ResourceNotFoundException;
import com.blogapp.model.Activity;
import com.blogapp.model.Comment;
import com.blogapp.model.Post;
import com.blogapp.model.User;
import com.blogapp.repository.ActivityRepository;
import com.blogapp.repository.CommentRepository;
import com.blogapp.repository.PostRepository;
import com.blogapp.repository.UserRepository;
import com.blogapp.service.ActivityService;
import com.blogapp.service.CommonService;
import com.blogapp.validation.ActivityValidation;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import static com.blogapp.constant.BlogAppConstant.ACTIVITY_EXCEPTION;
import static com.blogapp.constant.BlogAppConstant.COMMENT_EXCEPTION;
import static com.blogapp.constant.BlogAppConstant.EXCEPTION_FIELD;
import static com.blogapp.constant.BlogAppConstant.POST_EXCEPTION;
import static com.blogapp.constant.BlogAppConstant.USER_EXCEPTION;

@Service
@Slf4j
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    private final CommonService commonService;

    @Autowired
    public ActivityServiceImpl(ActivityRepository activityRepository, UserRepository userRepository, PostRepository postRepository, CommentRepository commentRepository, ModelMapper modelMapper, CommonService commonService) {
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
        this.commonService = commonService;
    }
    @Override
    @Transactional(rollbackOn = Exception.class)
    public ActivityDTO createActivity(ActivityDTO activityDTO) {
        log.info("createActivity method invoking");
        ActivityValidation.validateActivity(activityDTO);
        Integer userId = activityDTO.getUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_EXCEPTION, EXCEPTION_FIELD, userId));
        Activity activity = modelMapper.map(activityDTO, Activity.class);
        activity.setUser(user);
        if(!ObjectUtils.isEmpty(activityDTO.getPostId())) {
            Integer postId = activityDTO.getPostId();
            Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException(POST_EXCEPTION, EXCEPTION_FIELD, postId));
            activity.setPost(post);
        } else if (!ObjectUtils.isEmpty(activityDTO.getCommentId())) {
            Integer commentId = activityDTO.getCommentId();
            Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException(COMMENT_EXCEPTION, EXCEPTION_FIELD, commentId));
            activity.setComment(comment);
        }
        activity = activityRepository.save(activity);
        activityDTO = modelMapper.map(activity, ActivityDTO.class);
        UserDTO userDTO = commonService.convertUserToUserDTO(user);
        activityDTO.setUser(userDTO);
        return activityDTO;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ActivityDTO updateActivity(ActivityDTO activityDTO, Integer activityId) {
        log.info("updateActivity method invoking");
        Activity activity = activityRepository.findById(activityId).orElseThrow(() -> new ResourceNotFoundException(ACTIVITY_EXCEPTION, EXCEPTION_FIELD, activityId));
        ActivityValidation.validateActivity(activityDTO);
        activity.setActivityType(activityDTO.getActivityType());
        activity = activityRepository.save(activity);
        activityDTO = modelMapper.map(activity, ActivityDTO.class);
        UserDTO userDTO = commonService.convertUserToUserDTO(activity.getUser());
        activityDTO.setUser(userDTO);
        return activityDTO;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteActivity(Integer activityId) {
        log.info("deleteActivity method invoking");
        Activity activity = activityRepository.findById(activityId).orElseThrow(() -> new ResourceNotFoundException(ACTIVITY_EXCEPTION, EXCEPTION_FIELD, activityId));
        activityRepository.delete(activity);
    }
}
