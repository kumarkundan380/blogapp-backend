package com.blogapp.validation;

import com.blogapp.dto.ActivityDTO;
import com.blogapp.exception.BlogAppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

@Slf4j
public class ActivityValidation {

    public static void validateActivity(ActivityDTO activityDTO){
        log.info("validateActivity method invoking");
        if(ObjectUtils.isEmpty(activityDTO.getActivityType())){
            log.error("Activity Type is empty");
            throw new BlogAppException("Activity Type cannot be empty");
        } else if (ObjectUtils.isEmpty(activityDTO.getEntityType())) {
            log.error("Entity Type is empty");
            throw new BlogAppException("Entity Type cannot be empty");
        } else if (ObjectUtils.isEmpty(activityDTO.getUserId())) {
            log.error("User Id is empty");
            throw new BlogAppException("User Id cannot be empty");
        } else if (ObjectUtils.isEmpty(activityDTO.getPostId()) && ObjectUtils.isEmpty(activityDTO.getCommentId())) {
            log.error("Entity Id is empty");
            throw new BlogAppException("Entity Id cannot be empty");
        }
        log.info("validateActivity method called");
    }
}
