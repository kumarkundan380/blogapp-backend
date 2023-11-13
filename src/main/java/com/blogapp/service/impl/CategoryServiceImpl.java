package com.blogapp.service.impl;

import com.blogapp.dto.CategoryDTO;
import com.blogapp.exception.ResourceNotFoundException;
import com.blogapp.model.Category;
import com.blogapp.repository.CategoryRepository;
import com.blogapp.service.CategoryService;
import com.blogapp.validation.CategoryValidation;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.blogapp.constant.BlogAppConstant.CATEGORY_EXCEPTION;
import static com.blogapp.constant.BlogAppConstant.EXCEPTION_FIELD;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }
    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        log.info("createCategory method invoking");
        CategoryValidation.validateCategory(categoryDTO);
        Category category = modelMapper.map(categoryDTO, Category.class);
        category = categoryRepository.save(category);
        categoryDTO = modelMapper.map(category, CategoryDTO.class);
        return categoryDTO;
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Integer categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException(CATEGORY_EXCEPTION, EXCEPTION_FIELD, categoryId));
        CategoryValidation.validateCategory(categoryDTO);
        category.setCategoryTitle(categoryDTO.getCategoryTitle());
        category.setCategoryDescription(categoryDTO.getCategoryDescription());
        category = categoryRepository.save(category);
        categoryDTO = modelMapper.map(category, CategoryDTO.class);
        return categoryDTO;
    }

    @Override
    public CategoryDTO getCategoryById(Integer categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException(CATEGORY_EXCEPTION, EXCEPTION_FIELD, categoryId));
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public List<CategoryDTO> getAllCategory() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(category -> modelMapper.map(category,CategoryDTO.class)).collect(Collectors.toList());
    }

    @Override
    public void deleteCategory(Integer categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException(CATEGORY_EXCEPTION, EXCEPTION_FIELD, categoryId));
        categoryRepository.delete(category);
    }
}
