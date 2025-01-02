package com.example.hiker.dto;

import java.util.List;

public record ReviewInput(
        int mark,
        String description,
        List<String> photosUrl
) {
}
