package com.fitmedia.aiservice.service;

import com.fitmedia.aiservice.dto.RecommendationResponse;
import com.fitmedia.aiservice.model.Recommendation;
import com.fitmedia.aiservice.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;

    public List<RecommendationResponse> getUserRecommendation(Long userId) {
        return recommendationRepository.findByUserId(userId).stream().map(this::entityToResponse).collect(Collectors.toList());
    }

    public RecommendationResponse getActivityRecommendation(Long activityId) {
        Recommendation recommendation = recommendationRepository.findByActivityId(activityId);
        if(recommendation == null) throw new RuntimeException("No recommendation found for this activity: " + activityId);
        return entityToResponse(recommendation);
    }

    private RecommendationResponse entityToResponse(Recommendation entity){
        RecommendationResponse response = new RecommendationResponse();
        response.setId(entity.getId());
        response.setActivityId(entity.getActivityId());
        response.setUserId(entity.getUserId());
        response.setActivityType(entity.getActivityType());
        response.setRecommendation(entity.getRecommendation());
        response.setImprovements(entity.getImprovements());
        response.setSafety(entity.getSafety());
        response.setCreatedAt(entity.getCreatedAt());
        return response;
    }
}
