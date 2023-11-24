package com.blogapp.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.blogapp.constant.BlogAppConstant.CLOUDINARY_API_KEY;
import static com.blogapp.constant.BlogAppConstant.CLOUDINARY_API_SECRET;
import static com.blogapp.constant.BlogAppConstant.CLOUDINARY_CLOUD_NAME;
import static com.blogapp.constant.BlogAppConstant.CLOUDINARY_SECURE;

@Configuration
public class CloudinaryConfig {

    private final String cloudName;
    private final String apiKey;
    private final String apiSecret;

    public CloudinaryConfig(@Value("${blog-app.cloud.name}") String cloudName, @Value("${blog-app.api.key}") String apiKey, @Value("${blog-app.api.secret}") String apiSecret) {
        this.cloudName = cloudName;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    @Bean
    public Cloudinary cloudinary(){
        return new Cloudinary(ObjectUtils.asMap(
                CLOUDINARY_CLOUD_NAME, cloudName,
                CLOUDINARY_API_KEY, apiKey,
                CLOUDINARY_API_SECRET, apiSecret,
                CLOUDINARY_SECURE, true));


    }
}
