package com.example.hiker.model;

import lombok.Data;

import java.time.Instant;

@Data
public class Review {

    private int id;
    private String comment;
    private int rating;
    private Instant createdAt;
    private String authorId;
}
