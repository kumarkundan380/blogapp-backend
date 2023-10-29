package com.blogapp.service;
import com.blogapp.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {

    CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryDTO updateCategory(CategoryDTO categoryDTO, Integer categoryId);
    CategoryDTO getCategoryById(Integer categoryId);
    List<CategoryDTO> getAllCategory();
    void deleteCategory(Integer categoryId);

}
