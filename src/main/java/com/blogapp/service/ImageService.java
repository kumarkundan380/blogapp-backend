package com.blogapp.service;

import com.blogapp.dto.UserDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    UserDTO uploadImageOnS3(MultipartFile multipartFile, Integer userId);
    String uploadPresignedUrl(String fileName);
    String downloadPresignedUrl(String fileName);

}
