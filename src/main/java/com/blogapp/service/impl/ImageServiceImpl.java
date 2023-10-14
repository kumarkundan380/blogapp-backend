package com.blogapp.service.impl;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.blogapp.exception.BlogAppException;
import com.blogapp.service.ImageService;
import com.blogapp.util.DateUtils;
import com.cloudinary.Cloudinary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
@Slf4j
@Service
public class ImageServiceImpl implements ImageService {
    private final AmazonS3 amazonS3;

    private final String bucketName;

    private final String imagePath;
    private final Cloudinary cloudinary;

    @Autowired
    public ImageServiceImpl(AmazonS3 amazonS3, @Value("${application.bucket.name}") String bucketName,@Value("${image.path}") String imagePath, Cloudinary cloudinary) {
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;
        this.imagePath = imagePath;
        this.cloudinary = cloudinary;
    }

//    @Override
//    public UserDTO uploadImageOnS3(MultipartFile multipartFile, Integer userId) {
//        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_EXCEPTION, EXCEPTION_FIELD, userId));
//        // throw exception if file is empty
//        if(multipartFile.isEmpty()) {
//            throw new BlogAppException("Request Must contain File");
//        }
//        String fileName = UUID.randomUUID().toString().concat("-"+ multipartFile.getOriginalFilename());
//        File file = convertMultipartFileToFile(multipartFile);
//        amazonS3.putObject(new PutObjectRequest(bucketName, fileName,file));
//        String imageUrl = null;
//        try {
//            imageUrl = amazonS3.getUrl(bucketName,fileName).toURI().toString();
//        } catch (URISyntaxException e) {
//            amazonS3.deleteObject(bucketName,fileName);
//            throw new BlogAppException("Some Problem occured while fetching image url form s3");
//        }
//        user.setUserImage(imageUrl);
//        user = userRepository.save(user);
//        return commonService.convertUserToUserDTO(user);
//    }

    @Override
    public String uploadImageOnS3(MultipartFile multipartFile) {
        // throw exception if file is empty
        log.info("uploadImageOnS3 method invoking");
        if(multipartFile.isEmpty()) {
            log.info("Request Must contain File");
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
            log.info("Some Problem occured while fetching image url form s3");
            throw new BlogAppException("Some Problem occured while fetching image url form s3");
        }
        return imageUrl;
    }

    @Override
    public Map<String,String> uploadImageOnCloudinary(MultipartFile multipartFile) {
        log.info("uploadImageOnCloudinary method invoking");
        try {
            return cloudinary.uploader().upload(multipartFile.getBytes(), new HashMap<>());
        } catch (IOException e) {
            log.info("Image uploading failed");
            throw new BlogAppException("Image uploading failed");
        }
    }

    @Override
    public String uploadImageOnLocal(MultipartFile multipartFile) {
        log.info("uploadImageOnLocal method invoking");
        if(multipartFile.isEmpty()) {
            log.info("Request Must contain File");
            throw new BlogAppException("Request Must contain File");
        }
        String fileName = imagePath+UUID.randomUUID().toString().concat("-"+ multipartFile.getOriginalFilename());
        File file = new File(imagePath);
        if(!file.exists()){
            file.mkdir();
        }
        try {
            Files.copy(multipartFile.getInputStream(),Paths.get(fileName));
        } catch (IOException e){
            log.info("Some Problem occurred while uploading image");
            throw new BlogAppException("Some Problem occurred while uploading image");
        }
        return fileName;
    }


    @Override
    @Async
    public String downloadPresignedUrl(String fileName) {
        log.info("downloadPresignedUrl method invoking");
        if (!amazonS3.doesObjectExist(bucketName, fileName))
            throw new BlogAppException("File does not exist");
        return generatePresignedUrl(fileName, HttpMethod.GET);
    }

    @Override
    @Async
    public String uploadPresignedUrl(String fileName) {
        log.info("uploadPresignedUrl method invoking");
        fileName = UUID.randomUUID().toString() + fileName;
        return generatePresignedUrl(fileName, HttpMethod.PUT);
    }

    private String generatePresignedUrl(String fileName, HttpMethod httpMethod) {
        LocalDate date = LocalDate.now().plusDays(1); // Generated URL will be valid for 24 hours
        return amazonS3.generatePresignedUrl(bucketName, fileName, DateUtils.convertLocalDateToDate(date), httpMethod).toString();
    }


    private File convertMultipartFileToFile(MultipartFile file) {
        log.info("convertMultipartFileToFile method invoking");
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try(FileOutputStream fos = new FileOutputStream(convertedFile)){
            fos.write(file.getBytes());
        } catch (IOException e) {
            throw new BlogAppException("Error converting MultipartFile to File: "+ e);
        }
        return convertedFile;
    }

}
