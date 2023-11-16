package com.blogapp.service.impl;

import com.blogapp.dto.CategoryDTO;
import com.blogapp.dto.PostDTO;
import com.blogapp.dto.UserDTO;
import com.blogapp.enums.PostStatus;
import com.blogapp.exception.BlogAppException;
import com.blogapp.exception.ResourceNotFoundException;
import com.blogapp.model.Category;
import com.blogapp.model.Post;
import com.blogapp.model.User;
import com.blogapp.repository.CategoryRepository;
import com.blogapp.repository.PostRepository;
import com.blogapp.repository.UserRepository;
import com.blogapp.response.BlogAppPageableResponse;
import com.blogapp.service.CommonService;
import com.blogapp.service.ImageService;
import com.blogapp.service.PostService;
import com.blogapp.util.AuthorityUtil;
import com.blogapp.validation.PostValidation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.blogapp.constant.BlogAppConstant.CATEGORY_EXCEPTION;
import static com.blogapp.constant.BlogAppConstant.COMMON_SORT_FIELD;
import static com.blogapp.constant.BlogAppConstant.EXCEPTION_FIELD;
import static com.blogapp.constant.BlogAppConstant.POST_EXCEPTION;
import static com.blogapp.constant.BlogAppConstant.USER_EXCEPTION;

@Service
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    private final ImageService imageService;
    private final CommonService commonService;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, CategoryRepository categoryRepository, ModelMapper modelMapper, ObjectMapper objectMapper, ImageService imageService, CommonService commonService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.objectMapper = objectMapper;
        this.imageService = imageService;
        this.commonService = commonService;
    }
    @Override
    @Transactional(rollbackOn = Exception.class)
    public PostDTO createPost(MultipartFile image, String postData) {
        log.info("createPost method invoking");
        PostDTO postDTO;
        try{
            postDTO = objectMapper.readValue(postData, PostDTO.class);
        } catch (JsonProcessingException e){
            throw new BlogAppException("PostData is not in proper format");
        }
        PostValidation.validatePost(postDTO);
        Integer userId = postDTO.getUserId();
        Integer categoryId = postDTO.getCategoryId();
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_EXCEPTION, EXCEPTION_FIELD, userId));
        Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException(CATEGORY_EXCEPTION, EXCEPTION_FIELD, categoryId));
        String imageUrl = null;
        if(!ObjectUtils.isEmpty(image) && !image.isEmpty()) {
            Map<String,String> imageData = imageService.uploadImageOnCloudinary(image);
            if(imageData.containsKey("url")){
                imageUrl = imageData.get("url");
            }
        }
        Post post = modelMapper.map(postDTO, Post.class);
        post.setUser(user);
        post.setCategory(category);
        post.setImageUrl(imageUrl);
        post = postRepository.save(post);
        postDTO = commonService.convertPostToPostDTO(post);
        UserDTO userDTO = commonService.convertUserToUserDTO(user);
        CategoryDTO categoryDTO = modelMapper.map(category,CategoryDTO.class);
        postDTO.setUser(userDTO);
        postDTO.setCategory(categoryDTO);
        return postDTO;
    }

    @Override
    public PostDTO updatePost(MultipartFile image, String postData, Integer postId) {
        log.info("updatePost method invoking");
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException(POST_EXCEPTION, EXCEPTION_FIELD, postId));
        PostDTO postDTO;
        try{
            postDTO = objectMapper.readValue(postData, PostDTO.class);
        } catch (JsonProcessingException e){
            throw new BlogAppException("PostData is not in proper format");
        }
        PostValidation.validatePost(postDTO);
        if(!ObjectUtils.isEmpty(postDTO.getCategoryId())){
            Integer categoryId = postDTO.getCategoryId();
            Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException(CATEGORY_EXCEPTION, EXCEPTION_FIELD, categoryId));
            post.setCategory(category);
        }
        String imageUrl = null;
        if(!ObjectUtils.isEmpty(image) && !image.isEmpty()) {
            Map<String,String> imageData = imageService.uploadImageOnCloudinary(image);
            if(imageData.containsKey("url")){
                imageUrl = imageData.get("url");
            }
        }
        if(StringUtils.hasText(imageUrl)){
            post.setImageUrl(imageUrl);
        }
        post.setPostTitle(postDTO.getPostTitle());
        post.setPostContent(postDTO.getPostContent());
        if(!ObjectUtils.isEmpty(postDTO.getStatus())){
            if(AuthorityUtil.isAdminRole()){
                post.setPostStatus(postDTO.getStatus());
            } else {
                log.error("You do not have permission to perform this task");
                throw new BlogAppException("You do not have permission to perform this task");
            }
        }
        post = postRepository.save(post);
        postDTO = commonService.convertPostToPostDTO(post);
        UserDTO userDTO = commonService.convertUserToUserDTO(post.getUser());
        CategoryDTO categoryDTO = modelMapper.map(post.getCategory(),CategoryDTO.class);
        postDTO.setUser(userDTO);
        postDTO.setCategory(categoryDTO);
        return postDTO;
    }

    @Override
    public PostDTO getPostById(Integer postId) {
        Post post;
        if(AuthorityUtil.isAdminRole()){
            post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException(POST_EXCEPTION, EXCEPTION_FIELD, postId));
        } else {
            post = postRepository.getPostById(postId).orElseThrow(() -> new ResourceNotFoundException(POST_EXCEPTION, EXCEPTION_FIELD, postId));
        }
        return commonService.convertPostToPostDTO(post);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deletePost(Integer postId) {
        log.info("deletePost method invoking");
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException(POST_EXCEPTION, EXCEPTION_FIELD, postId));
        User user = post.getUser();
        if(AuthorityUtil.isAdminRole() || AuthorityUtil.isSameUser(user.getUserName())) {
            if(PostStatus.DELETED.getValue().equals(post.getPostStatus())){
                log.error("Post already deleted");
                throw new BlogAppException("Post already deleted");
            } else {
                postRepository.deletePost(postId);
            }
        } else {
            log.error("You cannot delete other user post");
            throw new BlogAppException("You cannot delete other user post");
        }
        log.info("deletePost method called");
    }

    @Override
    public BlogAppPageableResponse<?> getAllPost(Integer pageNumber, Integer pageSize) {
        log.info("getAllPost method invoking");
        return getAllPost(Arrays.asList(PostStatus.APPROVED,PostStatus.PENDING,PostStatus.DELETED), pageNumber,pageSize);
    }

    @Override
    public BlogAppPageableResponse<?> getAllApprovedPost(Integer pageNumber, Integer pageSize) {
        log.info("getAllApprovedPost method invoking");
        return getAllPost(Arrays.asList(PostStatus.APPROVED), pageNumber,pageSize);
    }

    @Override
    public BlogAppPageableResponse<?> getAllPendingPost(Integer pageNumber, Integer pageSize) {
        if(AuthorityUtil.isAdminRole()){
            log.info("getAllPendingPost method invoking");
            return getAllPost(Arrays.asList(PostStatus.PENDING), pageNumber,pageSize);
        } else {
            throw new BlogAppException("You do not have permission to perform this task");
        }
    }

    @Override
    public BlogAppPageableResponse<?> getAllDeletedPost(Integer pageNumber, Integer pageSize) {
        if(AuthorityUtil.isAdminRole()){
            log.info("getAllDeletedPost method invoking");
            return getAllPost(Arrays.asList(PostStatus.DELETED), pageNumber,pageSize);
        } else {
            throw new BlogAppException("You do not have permission to perform this task");
        }
    }

    private BlogAppPageableResponse<?> getAllPost(List<PostStatus> postStatus,Integer pageNumber,Integer pageSize) {
        log.info("getAllPost method invoking");
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, COMMON_SORT_FIELD));
        Page<Post> pagePosts = postRepository.findAllPostByStatus(postStatus, pageable);
        List<PostDTO> postDTOS = new ArrayList<>();
        List<Post> posts = null;
        if(!CollectionUtils.isEmpty(pagePosts.getContent())){
            posts = pagePosts.getContent();
        }
        if(!CollectionUtils.isEmpty(posts)){
            postDTOS = posts.stream().map(commonService::convertPostToPostDTO).collect(Collectors.toList());
        }
        log.info("getAllPost method called");
        return BlogAppPageableResponse.builder()
                .content(postDTOS)
                .pageNumber(ObjectUtils.isEmpty(pagePosts)?0:pagePosts.getNumber())
                .pageSize(ObjectUtils.isEmpty(pagePosts)?0:pagePosts.getSize())
                .totalElement(ObjectUtils.isEmpty(pagePosts)?0:pagePosts.getTotalElements())
                .totalPages(ObjectUtils.isEmpty(pagePosts)?0:pagePosts.getTotalPages())
                .isLast(ObjectUtils.isEmpty(pagePosts)?null:pagePosts.isLast())
                .isFirst(ObjectUtils.isEmpty(pagePosts)?null:pagePosts.isFirst())
                .build();
    }
}
