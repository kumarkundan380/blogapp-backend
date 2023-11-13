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
public class AdminInfoDTO implements Serializable {

    @Serial
    private static final Long serialVersionUID = 1L;
    private Long numberOfUser;
    private Long numberOfActiveUser;
    private Long numberOfPendingUser;
    private Long numberOfDeletedUser;
    private Long numberOfPost;
    private Long numberOfActivePost;
    private Long numberOfPendingPost;
    private Long numberOfDeletedPost;
    private Long numberOfCategory;
    private Long numberOfActiveCategory;
    private Long numberOfPendingCategory;
    private Long numberOfDeletedCategory;
}
