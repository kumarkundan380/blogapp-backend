package com.blogapp.service;

import com.blogapp.dto.CommentDTO;
import com.blogapp.response.BlogAppPageableResponse;

public interface CommentService {

    CommentDTO createComment(CommentDTO commentDTO);
    CommentDTO updateComment(CommentDTO commentDTO, Integer commentId);
    BlogAppPageableResponse<?> getAllComment(Integer pageNumber, Integer pageSize);
    BlogAppPageableResponse<?> getAllCommentByPost(Integer pageNumber, Integer pageSize,Integer postId);
    void deleteComment(Integer commentId);


}
