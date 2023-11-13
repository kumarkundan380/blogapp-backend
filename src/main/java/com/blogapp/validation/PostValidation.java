package com.blogapp.validation;

import com.blogapp.dto.PostDTO;
import com.blogapp.exception.BlogAppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Slf4j
public class PostValidation {

    public static void validatePost(PostDTO post){
        log.info("validatePost method invoking");
        if(!StringUtils.hasText(post.getPostTitle())){
            log.error("Post Title is empty");
            throw new BlogAppException("Post Title cannot be empty");
        } else if (!StringUtils.hasText(post.getPostContent())) {
            log.error("PostContent is empty");
            throw new BlogAppException("PostContent cannot be empty");
        } else if(ObjectUtils.isEmpty(post.getCategoryId())){
            log.error("CategoryId is empty");
            throw new BlogAppException("CategoryId cannot be empty");
        } else if(ObjectUtils.isEmpty(post.getUserId())){
            log.error("User Id is empty");
            throw new BlogAppException("User Id cannot be empty");
        }
        log.info("Removing extra spaces if any mandatory fields contains");
        post.setPostTitle(post.getPostTitle().trim());
        post.setPostContent(post.getPostContent().trim());
        log.info("validatePost method called");
    }

}
