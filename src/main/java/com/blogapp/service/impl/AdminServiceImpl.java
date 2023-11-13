package com.blogapp.service.impl;

import com.blogapp.dto.AdminInfoDTO;
import com.blogapp.enums.PostStatus;
import com.blogapp.enums.UserStatus;
import com.blogapp.exception.BlogAppException;
import com.blogapp.repository.CategoryRepository;
import com.blogapp.repository.PostRepository;
import com.blogapp.repository.UserRepository;
import com.blogapp.service.AdminService;
import com.blogapp.util.AuthorityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public AdminServiceImpl(PostRepository postRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }
    @Override
    public AdminInfoDTO getAdminInfo() {
        log.info("getAdminInfo method invoking");
        if(AuthorityUtil.isAdminRole()) {
            AdminInfoDTO adminInfoDTO = new AdminInfoDTO();
            List<String> userStatus = userRepository.getAllUserStatus();
            Long numberOfUser = userRepository.countAllUser();
            Long numberOfPendingUser = userRepository.countAllPendingUser(Boolean.FALSE);
            List<String> postStatus = postRepository.getAllPostStatus();
            Long numberOfCategory = categoryRepository.countAllCategory();
            Long numberOfActiveUser = userStatus.stream().filter(status -> UserStatus.ACTIVE.getValue().equalsIgnoreCase(status)).count();
            Long numberOfDeletedUser = userStatus.stream().filter(status -> UserStatus.DELETED.getValue().equalsIgnoreCase(status)).count();
            Long numberOfActivePost = postStatus.stream().filter(status -> PostStatus.APPROVED.getValue().equalsIgnoreCase(status)).count();
            Long numberOfPendingPost = postStatus.stream().filter(status -> PostStatus.PENDING.getValue().equalsIgnoreCase(status)).count();
            Long numberOfDeletedPost = postStatus.stream().filter(status -> PostStatus.DELETED.getValue().equalsIgnoreCase(status)).count();
            adminInfoDTO.setNumberOfUser(numberOfUser);
            adminInfoDTO.setNumberOfActiveUser(numberOfActiveUser);
            adminInfoDTO.setNumberOfPendingUser(numberOfPendingUser);
            adminInfoDTO.setNumberOfDeletedUser(numberOfDeletedUser);
            adminInfoDTO.setNumberOfPost(numberOfActivePost + numberOfPendingPost + numberOfDeletedPost);
            adminInfoDTO.setNumberOfActivePost(numberOfActivePost);
            adminInfoDTO.setNumberOfPendingPost(numberOfPendingPost);
            adminInfoDTO.setNumberOfDeletedPost(numberOfDeletedPost);
            adminInfoDTO.setNumberOfCategory(numberOfCategory);
            return adminInfoDTO;
        } else {
            log.error("You don't have permission to perform this Operation");
            throw new BlogAppException("You don't have permission to perform this Operation");
        }
    }
}
