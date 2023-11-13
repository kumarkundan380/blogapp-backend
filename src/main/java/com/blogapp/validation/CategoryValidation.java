package com.blogapp.validation;

import com.blogapp.dto.CategoryDTO;
import com.blogapp.exception.BlogAppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
@Slf4j
public class CategoryValidation {

    public static void validateCategory(CategoryDTO category){
        log.info("validateCategory method invoking");
        if(!StringUtils.hasText(category.getCategoryTitle())){
            log.error("Category Title is empty");
            throw new BlogAppException("Category Title cannot be empty");
        } else if (!StringUtils.hasText(category.getCategoryDescription())) {
            log.error("Category Description is empty");
            throw new BlogAppException("Category Description cannot be empty");
        }
        log.info("Removing extra spaces if any mandatory fields contains");
        category.setCategoryTitle(category.getCategoryTitle().trim());
        category.setCategoryDescription(category.getCategoryDescription().trim());
        log.info("validateCategory method called");
    }

}
