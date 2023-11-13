package com.blogapp.service.impl;

import com.blogapp.dto.CommentDTO;
import com.blogapp.exception.ResourceNotFoundException;
import com.blogapp.model.Comment;
import com.blogapp.model.Post;
import com.blogapp.model.User;
import com.blogapp.repository.CommentRepository;
import com.blogapp.repository.PostRepository;
import com.blogapp.repository.UserRepository;
import com.blogapp.response.BlogAppPageableResponse;
import com.blogapp.service.CommentService;
import com.blogapp.service.CommonService;
import com.blogapp.validation.CommentValidation;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

import static com.blogapp.constant.BlogAppConstant.COMMENT_EXCEPTION;
import static com.blogapp.constant.BlogAppConstant.COMMON_SORT_FIELD;
import static com.blogapp.constant.BlogAppConstant.EXCEPTION_FIELD;
import static com.blogapp.constant.BlogAppConstant.POST_EXCEPTION;
import static com.blogapp.constant.BlogAppConstant.USER_EXCEPTION;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final CommonService commonService;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository, ModelMapper modelMapper, CommonService commonService) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.commonService = commonService;
    }
    @Override
    @Transactional(rollbackOn = Exception.class)
    public CommentDTO createComment(CommentDTO commentDTO) {
        log.info("createComment method invoking");
        CommentValidation.validateComment(commentDTO);
        Integer userId = commentDTO.getUserId();
        Integer postId = commentDTO.getPostId();
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_EXCEPTION, EXCEPTION_FIELD, userId));
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException(POST_EXCEPTION, EXCEPTION_FIELD, postId));
        Comment comment = modelMapper.map(commentDTO,Comment.class);
        comment.setUser(user);
        comment.setPost(post);
        comment = commentRepository.save(comment);
        return commonService.convertCommentToCommentDTO(comment);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public CommentDTO updateComment(CommentDTO commentDTO, Integer commentId) {
        log.info("updateComment method invoking");
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException(COMMENT_EXCEPTION, EXCEPTION_FIELD, commentId));
        CommentValidation.validateComment(commentDTO);
        comment.setContent(commentDTO.getContent());
        comment = commentRepository.save(comment);
        return commonService.convertCommentToCommentDTO(comment);
    }

    @Override
    public BlogAppPageableResponse<?> getAllComment(Integer pageNumber, Integer pageSize) {
        log.info("getAllComment method invoking");
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, COMMON_SORT_FIELD));
        Page<Comment> pageComments = commentRepository.findAll(pageable);
        List<CommentDTO> commentDTOS = new ArrayList<>();
        if(!CollectionUtils.isEmpty(pageComments.getContent())){
            for(Comment comment : pageComments.getContent()){
                CommentDTO commentDTO = commonService.convertCommentToCommentDTO(comment);
                commentDTOS.add(commentDTO);
            }
        }
        log.info("getAllComment method called");
        return BlogAppPageableResponse.builder()
                .content(commentDTOS)
                .pageNumber(ObjectUtils.isEmpty(pageComments.getContent())?0:pageComments.getNumber())
                .pageSize(ObjectUtils.isEmpty(pageComments.getContent())?0:pageComments.getSize())
                .totalElement(ObjectUtils.isEmpty(pageComments.getContent())?0:pageComments.getTotalElements())
                .totalPages(ObjectUtils.isEmpty(pageComments.getContent())?0:pageComments.getTotalPages())
                .isLast(ObjectUtils.isEmpty(pageComments.getContent())?null:pageComments.isLast())
                .isFirst(ObjectUtils.isEmpty(pageComments.getContent())?null:pageComments.isFirst())
                .build();
    }

    @Override
    public BlogAppPageableResponse<?> getAllCommentByPost(Integer pageNumber, Integer pageSize,Integer postId) {
        log.info("getAllCommentByPost method invoking");
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, COMMON_SORT_FIELD));
        Page<Comment> pageComments = commentRepository.findAllCommentByPost(postId,pageable);
        List<CommentDTO> commentDTOS = new ArrayList<>();
        if(!CollectionUtils.isEmpty(pageComments.getContent())){
            for(Comment comment : pageComments.getContent()){
                CommentDTO commentDTO = commonService.convertCommentToCommentDTO(comment);
                commentDTOS.add(commentDTO);
            }
        }
        log.info("getAllComment method called");
        return BlogAppPageableResponse.builder()
                .content(commentDTOS)
                .pageNumber(ObjectUtils.isEmpty(pageComments.getContent())?0:pageComments.getNumber())
                .pageSize(ObjectUtils.isEmpty(pageComments.getContent())?0:pageComments.getSize())
                .totalElement(ObjectUtils.isEmpty(pageComments.getContent())?0:pageComments.getTotalElements())
                .totalPages(ObjectUtils.isEmpty(pageComments.getContent())?0:pageComments.getTotalPages())
                .isLast(ObjectUtils.isEmpty(pageComments.getContent())?null:pageComments.isLast())
                .isFirst(ObjectUtils.isEmpty(pageComments.getContent())?null:pageComments.isFirst())
                .build();
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteComment(Integer commentId) {
        log.info("deleteComment method invoking");
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException(COMMENT_EXCEPTION, EXCEPTION_FIELD, commentId));
        commentRepository.delete(comment);
    }
}
