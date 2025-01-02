package com.example.hiker.dto;

import java.util.List;

public record CommentDto(
        String userAvatarUrl,
        String username,
        int rating,
        String commentDate,
        String comment,
        List<String> photosUrl
) {
}
