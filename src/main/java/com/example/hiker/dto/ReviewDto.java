package com.example.hiker.dto;

import java.util.List;

public record ReviewDto(
        String userAvatarUrl,
        String username,
        int rating,
        String reviewDate,
        String description,
        List<String> photosUrl
) {
}
