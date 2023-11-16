package com.blogapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private Integer commentId;
    private String content;
    private UserDTO user;
    private Integer postId;
    private Integer userId;
    private Set<ActivityDTO> activities = new HashSet<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
