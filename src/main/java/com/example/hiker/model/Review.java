package com.example.hiker.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.List;

@Data
public class Review {

    @Id
    private int id;
    private String comment;
    private int rating;
    private Instant createdAt;
    private String authorEmail;
    private List<String> photosUrl;
}
