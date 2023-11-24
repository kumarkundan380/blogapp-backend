package com.blogapp.controller;

import com.blogapp.dto.CommentDTO;
import com.blogapp.enums.ResponseStatus;
import com.blogapp.response.BlogAppResponse;
import com.blogapp.service.CommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.blogapp.constant.BlogAppConstant.BASE_PATH_COMMENT;
import static com.blogapp.constant.BlogAppConstant.COMMENT_PARAMETER;
import static com.blogapp.constant.BlogAppConstant.PAGE_NUMBER;
import static com.blogapp.constant.BlogAppConstant.PAGE_NUMBER_VALUE;
import static com.blogapp.constant.BlogAppConstant.PAGE_SIZE;
import static com.blogapp.constant.BlogAppConstant.PAGE_SIZE_VALUE;
import static com.blogapp.constant.BlogAppConstant.POST_PARAMETER;

@RestController
@RequestMapping(BASE_PATH_COMMENT)
@Tag(name="Comment Controller", description = "API Related to Comment")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<BlogAppResponse<?>> createComment(@RequestBody CommentDTO commentDTO) {
        return new ResponseEntity<>(BlogAppResponse.builder()
                .status(ResponseStatus.SUCCESS)
                .message("Comment created successfully")
                .body(commentService.createComment(commentDTO))
                .build(),
                HttpStatus.CREATED);
    }

    @PutMapping("/{" + COMMENT_PARAMETER + "}")
    public ResponseEntity<BlogAppResponse<?>> updateComment(@RequestBody CommentDTO commentDTO, @PathVariable Integer commentId) {
        return new ResponseEntity<>(BlogAppResponse.builder()
                .status(ResponseStatus.SUCCESS)
                .message("Comment updated successfully")
                .body(commentService.updateComment(commentDTO, commentId))
                .build(),
                HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<BlogAppResponse<?>> getAllComments(@RequestParam(value = PAGE_NUMBER, defaultValue = PAGE_NUMBER_VALUE, required = false) Integer pageNumber,
                                                             @RequestParam(value = PAGE_SIZE, defaultValue = PAGE_SIZE_VALUE, required = false) Integer pageSize) {
        return new ResponseEntity<>(BlogAppResponse.builder()
                .status(ResponseStatus.SUCCESS)
                .message("Comment get successfully")
                .body(commentService.getAllComment(pageNumber,pageSize))
                .build(),
                HttpStatus.OK);
    }

    @GetMapping("/{"+POST_PARAMETER+"}")
    public ResponseEntity<BlogAppResponse<?>> getAllCommentByPost(@RequestParam(value = PAGE_NUMBER, defaultValue = PAGE_NUMBER_VALUE, required = false) Integer pageNumber,
                                                                  @RequestParam(value = PAGE_SIZE, defaultValue = PAGE_SIZE_VALUE, required = false) Integer pageSize,
                                                                  @PathVariable Integer postId) {
        return new ResponseEntity<>(BlogAppResponse.builder()
                .status(ResponseStatus.SUCCESS)
                .message("Comment get successfully")
                .body(commentService.getAllCommentByPost(pageNumber,pageSize,postId))
                .build(),
                HttpStatus.OK);
    }

    @DeleteMapping("/{" + COMMENT_PARAMETER + "}")
    public ResponseEntity<BlogAppResponse<?>> deleteComment(@PathVariable Integer commentId) {
        commentService.deleteComment(commentId);
        return new ResponseEntity<>(BlogAppResponse.builder()
                .status(ResponseStatus.SUCCESS)
                .message("Comment deleted successfully")
                .build(),
                HttpStatus.OK);
    }

}
