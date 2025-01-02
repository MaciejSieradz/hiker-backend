package com.example.hiker.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document("Trail")
@Data
public class Trail {

    @Id
    private String id;
    private String title;
    private String difficulty;
    private double distance;
    private int elevationGain;
    private int maxHeight;
    private String description;
    private String gpxUrl;
    private EstimatedHikingTime estimatedHikingTime;
    private String userEmail;
    private List<String> photosUrl;
    private List<Review> reviews = new ArrayList<>();

    @CreatedDate
    private Instant createdAt;
}
