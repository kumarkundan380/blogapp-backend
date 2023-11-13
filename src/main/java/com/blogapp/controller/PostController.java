package com.blogapp.controller;

import com.blogapp.enums.ResponseStatus;
import com.blogapp.response.BlogAppResponse;
import com.blogapp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static com.blogapp.constant.BlogAppConstant.APPROVED_POST;
import static com.blogapp.constant.BlogAppConstant.BASE_PATH_POST;
import static com.blogapp.constant.BlogAppConstant.DELETED_POST;
import static com.blogapp.constant.BlogAppConstant.IMAGE_PARAMETER;
import static com.blogapp.constant.BlogAppConstant.PAGE_NUMBER;
import static com.blogapp.constant.BlogAppConstant.PAGE_NUMBER_VALUE;
import static com.blogapp.constant.BlogAppConstant.PAGE_SIZE;
import static com.blogapp.constant.BlogAppConstant.PAGE_SIZE_VALUE;
import static com.blogapp.constant.BlogAppConstant.PENDING_POST;
import static com.blogapp.constant.BlogAppConstant.POST_DATA;
import static com.blogapp.constant.BlogAppConstant.POST_PARAMETER;

@RestController
@RequestMapping(value = BASE_PATH_POST)
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<BlogAppResponse<?>> createPost(@RequestParam(value = IMAGE_PARAMETER, required = false) MultipartFile image,
                                                           @RequestParam(POST_DATA) String postData) {
        return new ResponseEntity<>(BlogAppResponse.builder()
                .status(ResponseStatus.SUCCESS)
                .message("Post created successfully.")
                .body(postService.createPost(image,postData))
                .build(),
                HttpStatus.CREATED);
    }

    @PutMapping("/{" + POST_PARAMETER + "}")
    public ResponseEntity<BlogAppResponse<?>> updatePost(@RequestParam(value = IMAGE_PARAMETER, required = false) MultipartFile image,
                                                         @RequestParam(POST_DATA) String postData, @PathVariable Integer postId) {
        return new ResponseEntity<>(BlogAppResponse.builder()
                .status(ResponseStatus.SUCCESS)
                .message("Post updated successfully.")
                .body(postService.updatePost(image,postData,postId))
                .build(),
                HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<BlogAppResponse<?>> findAllPost(
            @RequestParam(value = PAGE_NUMBER, defaultValue = PAGE_NUMBER_VALUE, required = false) Integer pageNumber,
            @RequestParam(value = PAGE_SIZE, defaultValue = PAGE_SIZE_VALUE, required = false) Integer pageSize) {
        return new ResponseEntity<>(BlogAppResponse.builder()
                .status(ResponseStatus.SUCCESS)
                .message("Fetch Post Successfully")
                .body(postService.getAllPost(pageNumber, pageSize))
                .build(),
                HttpStatus.OK);
    }


    @GetMapping(APPROVED_POST)
    public ResponseEntity<BlogAppResponse<?>> findAllApprovedPost(
            @RequestParam(value = PAGE_NUMBER, defaultValue = PAGE_NUMBER_VALUE, required = false) Integer pageNumber,
            @RequestParam(value = PAGE_SIZE, defaultValue = PAGE_SIZE_VALUE, required = false) Integer pageSize) {
        return new ResponseEntity<>(BlogAppResponse.builder()
                .status(ResponseStatus.SUCCESS)
                .message("Fetch Post Successfully")
                .body(postService.getAllApprovedPost(pageNumber, pageSize))
                .build(),
                HttpStatus.OK);
    }

    @GetMapping(PENDING_POST)
    public ResponseEntity<BlogAppResponse<?>> findAllPendingPost(
            @RequestParam(value = PAGE_NUMBER, defaultValue = PAGE_NUMBER_VALUE, required = false) Integer pageNumber,
            @RequestParam(value = PAGE_SIZE, defaultValue = PAGE_SIZE_VALUE, required = false) Integer pageSize) {
        return new ResponseEntity<>(BlogAppResponse.builder()
                .status(ResponseStatus.SUCCESS)
                .message("Fetch Post Successfully")
                .body(postService.getAllPendingPost(pageNumber, pageSize))
                .build(),
                HttpStatus.OK);
    }

    @GetMapping(DELETED_POST)
    public ResponseEntity<BlogAppResponse<?>> findAllDeletedPost(
            @RequestParam(value = PAGE_NUMBER, defaultValue = PAGE_NUMBER_VALUE, required = false) Integer pageNumber,
            @RequestParam(value = PAGE_SIZE, defaultValue = PAGE_SIZE_VALUE, required = false) Integer pageSize) {
        return new ResponseEntity<>(BlogAppResponse.builder()
                .status(ResponseStatus.SUCCESS)
                .message("Fetch Post Successfully")
                .body(postService.getAllDeletedPost(pageNumber, pageSize))
                .build(),
                HttpStatus.OK);
    }

    @GetMapping("/{" + POST_PARAMETER + "}")
    public ResponseEntity<BlogAppResponse<?>> getOnePost(@PathVariable Integer postId) {
        return new ResponseEntity<>(BlogAppResponse.builder()
                .status(ResponseStatus.SUCCESS)
                .message("Fetch Post Successfully")
                .body(postService.getPostById(postId))
                .build(),
                HttpStatus.OK);
    }

    @DeleteMapping("/{" + POST_PARAMETER + "}")
    public ResponseEntity<BlogAppResponse<?>> deletePost(@PathVariable Integer postId) {
        postService.deletePost(postId);
        return new ResponseEntity<>(BlogAppResponse.builder()
                .status(ResponseStatus.SUCCESS)
                .message("Post Deleted Successfully")
                .build(),
                HttpStatus.OK);
    }

}
