package com.blogapp.service;

import com.blogapp.dto.ActivityDTO;

public interface ActivityService {

    ActivityDTO createActivity(ActivityDTO activityDTO);

    ActivityDTO updateActivity(ActivityDTO activityDTO,Integer activityId);

    void deleteActivity(Integer activityId);

}