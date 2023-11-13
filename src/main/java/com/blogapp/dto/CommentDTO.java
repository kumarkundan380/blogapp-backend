package com.blogapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties( value = {"post"}, allowSetters =  true )
public class CommentDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private Integer commentId;
    private String content;
    private PostDTO post;
    private UserDTO user;
    private Integer postId;
    private Integer userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
