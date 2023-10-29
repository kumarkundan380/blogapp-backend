package com.blogapp.service;

import com.blogapp.dto.PostDTO;
import com.blogapp.response.BlogAppPageableResponse;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {

    PostDTO createPost(MultipartFile image, String postData);
    PostDTO updatePost(MultipartFile image, String postData, Integer postId);
    PostDTO getPostById(Integer postId);
    void deletePost(Integer postId);
    BlogAppPageableResponse<?> getAllPost(Integer pageNumber, Integer pageSize);
    BlogAppPageableResponse<?> getAllApprovedPost(Integer pageNumber, Integer pageSize);
    BlogAppPageableResponse<?> getAllPendingPost(Integer pageNumber, Integer pageSize);
    BlogAppPageableResponse<?> getAllDeletedPost(Integer pageNumber, Integer pageSize);

}
