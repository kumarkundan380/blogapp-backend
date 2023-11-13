package com.blogapp.controller;

import com.blogapp.dto.CategoryDTO;
import com.blogapp.enums.ResponseStatus;
import com.blogapp.response.BlogAppResponse;
import com.blogapp.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.blogapp.constant.BlogAppConstant.BASE_PATH_CATEGORY;
import static com.blogapp.constant.BlogAppConstant.CATEGORY_PARAMETER;

@RestController
@RequestMapping(value = BASE_PATH_CATEGORY)
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<BlogAppResponse<?>> createCategory(@RequestBody CategoryDTO categoryDTO) {
        return new ResponseEntity<>(BlogAppResponse.builder()
                .status(ResponseStatus.SUCCESS)
                .message("Category created successfully")
                .body(categoryService.createCategory(categoryDTO))
                .build(),
                HttpStatus.CREATED);
    }

    @PutMapping("/{" + CATEGORY_PARAMETER + "}")
    public ResponseEntity<BlogAppResponse<?>> updateCategory(@RequestBody CategoryDTO categoryDTO, @PathVariable Integer categoryId) {
        return new ResponseEntity<>(BlogAppResponse.builder()
                .status(ResponseStatus.SUCCESS)
                .message("Category updated successfully")
                .body(categoryService.updateCategory(categoryDTO, categoryId))
                .build(),
                HttpStatus.OK);
    }

    @GetMapping("/{" + CATEGORY_PARAMETER + "}")
    public ResponseEntity<BlogAppResponse<?>> getOneCategory(@PathVariable Integer categoryId) {
        return new ResponseEntity<>(BlogAppResponse.builder()
                .status(ResponseStatus.SUCCESS)
                .message("Category get successfully")
                .body(categoryService.getCategoryById(categoryId))
                .build(),
                HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<BlogAppResponse<?>> getCategories() {
        return new ResponseEntity<>(BlogAppResponse.builder()
                .status(ResponseStatus.SUCCESS)
                .message("Category get successfully")
                .body(categoryService.getAllCategory())
                .build(),
                HttpStatus.OK);
    }

    @DeleteMapping("/{" + CATEGORY_PARAMETER + "}")
    public ResponseEntity<BlogAppResponse<?>> deleteCategory(@PathVariable Integer categoryId) {
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(BlogAppResponse.builder()
                .status(ResponseStatus.SUCCESS)
                .message("Category deleted successfully")
                .build(),
                HttpStatus.OK);
    }


}
