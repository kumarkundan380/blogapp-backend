package com.blogapp.controller;

import com.blogapp.enums.ResponseStatus;
import com.blogapp.response.BlogAppResponse;
import com.blogapp.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.blogapp.constant.BlogAppConstant.BASE_PATH_ADMIN;
@RestController
@RequestMapping(BASE_PATH_ADMIN)
public class AdminInfoController {

    private final AdminService adminService;

    @Autowired
    public AdminInfoController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public ResponseEntity<BlogAppResponse<?>> getAdminInfo() {
        return new ResponseEntity<>(BlogAppResponse.builder()
                .status(ResponseStatus.SUCCESS)
                .message("Fetch Admin Information Successfully")
                .body(adminService.getAdminInfo())
                .build(),
                HttpStatus.OK);
    }
}
