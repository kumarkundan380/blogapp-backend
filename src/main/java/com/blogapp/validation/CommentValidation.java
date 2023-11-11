package com.blogapp.validation;

import com.blogapp.dto.CommentDTO;
import com.blogapp.exception.BlogAppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Slf4j
public class CommentValidation {

    public static void validateComment(CommentDTO commentDTO){
        log.info("validateComment method invoking");
        if(!StringUtils.hasText(commentDTO.getContent())){
            log.error("Comment content is empty");
            throw new BlogAppException("Comment content cannot be empty");
        } else if (ObjectUtils.isEmpty(commentDTO.getPostId())) {
            log.error("Post Id is empty");
            throw new BlogAppException("Post Id cannot be empty");
        } else if(ObjectUtils.isEmpty(commentDTO.getUserId())){
            log.error("User id is empty");
            throw new BlogAppException("User Id cannot be empty");
        }
        log.info("Removing extra spaces if any mandatory fields contains");
        commentDTO.setContent(commentDTO.getContent().trim());
        log.info("validateComment method called");
    }

}
