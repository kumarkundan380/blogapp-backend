package com.blogapp.dto;

import com.blogapp.enums.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private Integer postId;
    private String postTitle;
    private String postContent;
    private String imageUrl;
    private CategoryDTO category;
    private UserDTO user;
    private List<CommentDTO> comments = new ArrayList<>();
    private Integer userId;
    private Integer categoryId;
    private PostStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
