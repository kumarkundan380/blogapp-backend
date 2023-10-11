package com.blogapp.service.impl;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.blogapp.dto.UserDTO;
import com.blogapp.exception.BlogAppException;
import com.blogapp.exception.ResourceNotFoundException;
import com.blogapp.model.User;
import com.blogapp.repository.UserRepository;
import com.blogapp.service.CommonService;
import com.blogapp.service.ImageService;
import com.blogapp.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import static com.blogapp.constant.BlogAppConstant.EXCEPTION_FIELD;
import static com.blogapp.constant.BlogAppConstant.USER_EXCEPTION;
@Service
public class ImageServiceImpl implements ImageService {

    private final UserRepository userRepository;

    private final AmazonS3 amazonS3;

    private final String bucketName;

    private final CommonService commonService;

    @Autowired
    public ImageServiceImpl(UserRepository userRepository, AmazonS3 amazonS3, @Value("${application.bucket.name}") String bucketName, CommonService commonService) {
        this.userRepository = userRepository;
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;
        this.commonService = commonService;
    }

    @Override
    public UserDTO uploadImageOnS3(MultipartFile multipartFile, Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_EXCEPTION, EXCEPTION_FIELD, userId));
        // throw exception if file is empty
        if(multipartFile.isEmpty()) {
            throw new BlogAppException("Request Must contain File");
        }
        String fileName = UUID.randomUUID().toString().concat("-"+ multipartFile.getOriginalFilename());
        File file = convertMultipartFileToFile(multipartFile);
        amazonS3.putObject(new PutObjectRequest(bucketName, fileName,file));
        String imageUrl = null;
        try {
            imageUrl = amazonS3.getUrl(bucketName,fileName).toURI().toString();
        } catch (URISyntaxException e) {
            amazonS3.deleteObject(bucketName,fileName);
            throw new BlogAppException("Some Problem occured while fetching image url form s3");
        }
        user.setUserImage(imageUrl);
        user = userRepository.save(user);
        return commonService.convertUserToUserDTO(user);
    }

    @Override
    @Async
    public String downloadPresignedUrl(String fileName) {
        if (!amazonS3.doesObjectExist(bucketName, fileName))
            throw new BlogAppException("File does not exist");
        return generatePresignedUrl(fileName, HttpMethod.GET);
    }

    @Override
    @Async
    public String uploadPresignedUrl(String fileName) {
        fileName = UUID.randomUUID().toString() + fileName;
        return generatePresignedUrl(fileName, HttpMethod.PUT);
    }

    private String generatePresignedUrl(String fileName, HttpMethod httpMethod) {
        LocalDate date = LocalDate.now().plusDays(1); // Generated URL will be valid for 24 hours
        return amazonS3.generatePresignedUrl(bucketName, fileName, DateUtils.convertLocalDateToDate(date), httpMethod).toString();
    }


    private File convertMultipartFileToFile(MultipartFile file) {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try(FileOutputStream fos = new FileOutputStream(convertedFile)){
            fos.write(file.getBytes());
        } catch (IOException e) {
            throw new BlogAppException("Error converting MultipartFile to File: "+ e);
        }
        return convertedFile;
    }

}
