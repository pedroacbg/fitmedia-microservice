package com.fitmedia.aiservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class Activity {

    private Long id;
    private Long userId;
    private String type;
    private Integer duration;
    private Integer caloriesBurned;
    private LocalDateTime startTime;
    private Map<String, Object> additionalMetrics = new HashMap<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
