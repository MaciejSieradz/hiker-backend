package com.example.hiker.dto;

import java.util.List;

public record TrailInput(
        String title,
        String difficulty,
        EstimatedHikingTimeInput estimatedHikingTime,
        String description,
        String gpxTrailUrl,
        int elevation,
        double distance,
        int maxHeight,
        List<String> photosUrl
) {
}
