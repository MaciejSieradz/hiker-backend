package com.example.hiker.dto;


import com.example.hiker.model.EstimatedHikingTime;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TrailDto {
    private final String id;
    private final List<String> photos;
    private final String title;
    private final String difficulty;
    private final double rating;
    private final int numberOfRatings;
    private final double distance;
    private final int elevationGain;
    private final int maxHeight;
    private final String description;
    private final EstimatedHikingTime estimatedHikingTime;
    private final List<CommentDto> comments;
    private final String gpxUrl;
    private final boolean isMarked;
}
