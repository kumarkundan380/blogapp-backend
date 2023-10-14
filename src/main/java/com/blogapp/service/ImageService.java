package com.blogapp.service;

import com.blogapp.dto.UserDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface ImageService {

    //UserDTO uploadImageOnS3(MultipartFile multipartFile, Integer userId);

    String uploadImageOnS3(MultipartFile multipartFile);

    Map<String,String> uploadImageOnCloudinary(MultipartFile multipartFile);
    String uploadImageOnLocal(MultipartFile multipartFile);
    String uploadPresignedUrl(String fileName);
    String downloadPresignedUrl(String fileName);

}
