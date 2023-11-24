package com.blogapp.controller;

import com.blogapp.dto.ActivityDTO;
import com.blogapp.enums.ResponseStatus;
import com.blogapp.response.BlogAppResponse;
import com.blogapp.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.blogapp.constant.BlogAppConstant.ACTIVITY_PARAMETER;
import static com.blogapp.constant.BlogAppConstant.BASE_PATH_ACTIVITY;

@RestController
@RequestMapping(BASE_PATH_ACTIVITY)
public class ActivityController {

    private final ActivityService activityService;

    @Autowired
    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @PostMapping
    public ResponseEntity<BlogAppResponse<?>> createActivity(@RequestBody ActivityDTO activityDTO) {
        return new ResponseEntity<>(BlogAppResponse.builder()
                .status(ResponseStatus.SUCCESS)
                .message("Activity created successfully")
                .body(activityService.createActivity(activityDTO))
                .build(),
                HttpStatus.CREATED);
    }

    @PutMapping("/{" + ACTIVITY_PARAMETER + "}")
    public ResponseEntity<BlogAppResponse<?>> updateActivity(@RequestBody ActivityDTO activityDTO, @PathVariable Integer activityId) {
        return new ResponseEntity<>(BlogAppResponse.builder()
                .status(ResponseStatus.SUCCESS)
                .message("Activity updated successfully")
                .body(activityService.updateActivity(activityDTO,activityId))
                .build(),
                HttpStatus.CREATED);
    }

    @DeleteMapping("/{" + ACTIVITY_PARAMETER + "}")
    public ResponseEntity<BlogAppResponse<?>> deleteComment(@PathVariable Integer activityId) {
        activityService.deleteActivity(activityId);
        return new ResponseEntity<>(BlogAppResponse.builder()
                .status(ResponseStatus.SUCCESS)
                .message("Activity deleted successfully")
                .build(),
                HttpStatus.OK);
    }

}
