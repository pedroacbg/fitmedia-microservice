package com.fitmedia.activityservice.dto;

import com.fitmedia.activityservice.model.ActivityType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class ActivityResponse {

    private Long id;
    private String userId;
    private ActivityType type;
    private Integer duration;
    private Integer caloriesBurned;
    private LocalDateTime startTime;
    private Map<String, Object> additionalMetrics = new HashMap<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
