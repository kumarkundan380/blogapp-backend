package com.blogapp.constant;

public class BlogAppConstant {
    private BlogAppConstant () {}

    public static final String LOCAL_BASE_PATH = "http://localhost:8080";
    public static final String COMMON_PATH = "/api/v1";
    public static final String ROLES_PATH = "/roles";
    public static final String ADDRESS_PATH = "/address";
    public static final String CATEGORY_PATH = "/category";
    public static final String PAGE_NUMBER = "pageNumber";
    public static final String PAGE_SIZE = "size";
    public static final String PAGE_NUMBER_VALUE = "0";
    public static final String PAGE_SIZE_VALUE = "10";
    public static final String POST_SORT_FIELD = "createdAt";
    public static final String USER_SORT_FIELD = "firstName";
    public static final String CATEGORY_SORT_FIELD = "categoryTitle";
    public static final String CATEGORY_EXCEPTION = "Category";
    public static final String POST_EXCEPTION = "Post";
    public static final String USER_EXCEPTION = "User";
    public static final String ADDRESS_EXCEPTION = "Address";
    public static final String COMMENT_EXCEPTION = "Comment";
    public static final String EXCEPTION_FIELD = "id";
    public static final String DEFAULT_IMAGE = "default.jpg";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_PREFIX = "Bearer ";
    public static final String BASE_PATH_AUTH = COMMON_PATH + "/auth";
    public static final String BASE_PATH_IMAGE = COMMON_PATH + "/image";

    public static final String IMAGE_PARAMETER = "image";

    public static final String USER_DATA = "userData";

    public static final String PRESIGNED_PARAMETER = "fileName";

    public static final String UPLOAD_PRESIGNED_URL = "/upload/presigned/url";
    public static final String DOWNLOAD_PRESIGNED_URL = "/download/presigned/url";

    public static final String LOGIN = "/login";

    public static final String BASE_PATH_CATEGORY = COMMON_PATH + CATEGORY_PATH;
    public static final String CATEGORY_PARAMETER = "categoryId";

    public static final String BASE_PATH_COMMENT = COMMON_PATH + "/comment";
    public static final String COMMENT_PARAMETER = "commentId";

    public static final String BASE_PATH_USER = COMMON_PATH + "/users";
    public static final String CHANGE_PASSWORD = "/change-password";
    public static final String VERIFY_USER = "/verify";
    public static final String USER_PARAMETER = "userId";
    public static final String ADDRESS_PARAMETER = "addressId";
    public static final String TOKEN_PARAMETER = "token";
    public static final String BASE_PATH_POST = COMMON_PATH + "/post";
    public static final String SEARCH_POST = "/search";
    public static final String UPLOAD_IMAGE = "/image/upload";
    public static final String DOWNLOAD_IMAGE = "/download/image";
    public static final String POST_PARAMETER = "postId";

    public static final String ATTACHMENT_PATH = "/home/kundan/SpringBoot/BlogAppBackendSigleModel/src/main/resources/logo/logo.png";
    public static final String CLOUDINARY_CLOUD_NAME = "cloud_name";
    public static final String CLOUDINARY_API_KEY = "api_key";
    public static final String CLOUDINARY_API_SECRET = "api_secret";
    public static final String CLOUDINARY_SECURE= "secure";

    public static final String[] PUBLIC_URLS = {
            BASE_PATH_AUTH + LOGIN,
            BASE_PATH_USER ,
            BASE_PATH_USER + VERIFY_USER +"/**",
            "/v3/api-docs",
            "/v2/api-docs",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/webjars/**"
    };

    public static final String[] ADMIN_URLS = {
            BASE_PATH_USER + "/**"
    };

}
