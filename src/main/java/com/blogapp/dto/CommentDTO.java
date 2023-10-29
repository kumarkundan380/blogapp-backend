package com.blogapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer commentId;

    private String content;

    private PostDTO post;

    private UserDTO user;


}
