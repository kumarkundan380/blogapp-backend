package com.blogapp.service;

import com.blogapp.dto.CommentDTO;
import com.blogapp.dto.PostDTO;
import com.blogapp.dto.UserDTO;
import com.blogapp.model.Comment;
import com.blogapp.model.Post;
import com.blogapp.model.User;

public interface CommonService {
    UserDTO convertUserToUserDTO(User user);

    PostDTO convertPostToPostDTO(Post post);

    CommentDTO convertCommentToCommentDTO(Comment comment);
}
