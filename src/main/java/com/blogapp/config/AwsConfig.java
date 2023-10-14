package com.blogapp.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {

    private final String accessKey;

    private final String secretKey;

    private final String region;

    public AwsConfig(@Value("${spring.cloud.aws.credentials.accessKey}") String accessKey, @Value("${spring.cloud.aws.credentials.secretKey}") String secretKey, @Value("${spring.cloud.aws.region.static}") String region) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.region = region;
    }

    @Bean
    public AmazonS3 amazonS3() {
        return AmazonS3Client.builder()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .withRegion(region)
                .build();
    }

}
