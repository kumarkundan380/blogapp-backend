package com.blogapp.controller;

import com.blogapp.enums.ResponseStatus;
import com.blogapp.response.BlogAppResponse;
import com.blogapp.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static com.blogapp.constant.BlogAppConstant.BASE_PATH_IMAGE;
import static com.blogapp.constant.BlogAppConstant.UPLOAD_PRESIGNED_URL;
import static com.blogapp.constant.BlogAppConstant.DOWNLOAD_PRESIGNED_URL;
import static com.blogapp.constant.BlogAppConstant.IMAGE_PARAMETER;
import static com.blogapp.constant.BlogAppConstant.PRESIGNED_PARAMETER;
import static com.blogapp.constant.BlogAppConstant.USER_PARAMETER;

@RestController
@RequestMapping(value = BASE_PATH_IMAGE)
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/{" + USER_PARAMETER + "}")
    public ResponseEntity<BlogAppResponse<?>> uploadImage(@RequestParam(IMAGE_PARAMETER) MultipartFile image,
                                                          @PathVariable Integer userId) {
        return new ResponseEntity<>(BlogAppResponse.builder()
                .status(ResponseStatus.SUCCESS)
                .message("Images Uploaded successfully")
                .body(imageService.uploadImageOnS3(image,userId))
                .build(),
                HttpStatus.CREATED);
    }

    @GetMapping(UPLOAD_PRESIGNED_URL)
    public ResponseEntity<BlogAppResponse<?>> uploadPresignedUrl(@RequestParam(PRESIGNED_PARAMETER) String fileName) {
        return new ResponseEntity<>(BlogAppResponse.builder()
                .status(ResponseStatus.SUCCESS)
                .message("Fetch Upload Presigned Url successfully")
                .body(imageService.uploadPresignedUrl(fileName))
                .build(),
                HttpStatus.OK);
    }

    @GetMapping(DOWNLOAD_PRESIGNED_URL)
    public ResponseEntity<BlogAppResponse<?>> downloadPresignedUrl(@RequestParam(PRESIGNED_PARAMETER) String extension) {
        return new ResponseEntity<>(BlogAppResponse.builder()
                .status(ResponseStatus.SUCCESS)
                .message("Fetch Download Presigned Url successfully")
                .body(imageService.downloadPresignedUrl(extension))
                .build(),
                HttpStatus.OK);
    }

}
