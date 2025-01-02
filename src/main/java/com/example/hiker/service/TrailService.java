package com.example.hiker.service;

import com.example.hiker.dto.ReviewInput;
import com.example.hiker.dto.TrailDto;
import com.example.hiker.dto.TrailInput;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TrailService {

    Flux<TrailDto> getAllTrails();
    Flux<TrailDto> getUserTrails(String email);
    Mono<TrailDto> getTrailById(String id, String email);
    Mono<TrailDto> createTrail(TrailInput trailInput, String email);
    Mono<Boolean> addReview(String trailId, ReviewInput reviewInput, String email);
}
