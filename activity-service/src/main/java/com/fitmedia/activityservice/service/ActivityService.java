package com.fitmedia.activityservice.service;

import com.fitmedia.activityservice.dto.ActivityRequest;
import com.fitmedia.activityservice.dto.ActivityResponse;
import com.fitmedia.activityservice.model.Activity;
import com.fitmedia.activityservice.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;

    public ActivityResponse trackActivity(ActivityRequest request) {
        Activity activity =  requestToEntity(request);
        Activity savedActivity = activityRepository.save(activity);
        return entityToResponse(savedActivity);
    }

    public List<ActivityResponse> getUserActivities(String userId) {
        List<Activity> activities = activityRepository.findByUserId(userId);
        return activities.stream().map(this::entityToResponse).collect(Collectors.toList());
    }

    public ActivityResponse getActivityById(Long activityId) {
        return activityRepository.findById(activityId).map(this::entityToResponse).orElseThrow(() -> new RuntimeException("Activity not found"));
    }

    private Activity requestToEntity(ActivityRequest request){
        Activity activity = new Activity();
        activity.setUserId(request.getUserId());
        activity.setDuration(request.getDuration());
        activity.setType(request.getType());
        activity.setCaloriesBurned(request.getCaloriesBurned());
        activity.setAdditionalMetrics(request.getAdditionalMetrics());
        activity.setStartTime(request.getStartTime());
        return activity;
    }

    private ActivityResponse entityToResponse(Activity activity){
        ActivityResponse response = new ActivityResponse();
        response.setId(activity.getId());
        response.setUserId(activity.getUserId());
        response.setDuration(activity.getDuration());
        response.setType(activity.getType());
        response.setCaloriesBurned(activity.getCaloriesBurned());
        response.setAdditionalMetrics(activity.getAdditionalMetrics());
        response.setStartTime(activity.getStartTime());
        response.setCreatedAt(activity.getCreatedAt());
        response.setUpdatedAt(activity.getUpdatedAt());
        return response;
    }
}
