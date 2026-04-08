package com.fitmedia.aiservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "tb_recommendation")
@Data
@NoArgsConstructor
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long activityId;
    private String userId;
    private String activityType;

    @Column(length = 3000)
    private String recommendation;

    @Column(length = 3000)
    private List<String> improvements;

    @Column(length = 3000)
    private List<String> suggestions;

    @Column(length = 3000)
    private List<String> safety;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
