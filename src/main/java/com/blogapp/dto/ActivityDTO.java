package com.blogapp.dto;

import com.blogapp.enums.ActivityType;
import com.blogapp.enums.EntityType;
import com.blogapp.model.Activity;
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
public class ActivityDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private Integer activityId;
    private ActivityType activityType;
    private EntityType entityType;
    private UserDTO user;
    private Integer postId;
    private Integer commentId;
    private Integer userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
