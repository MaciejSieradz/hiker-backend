package com.example.hiker.dto;

import lombok.Data;

public record CommentDto(String userAvatarUrl, String username, int rating, String commentDate, String comment) {
}
