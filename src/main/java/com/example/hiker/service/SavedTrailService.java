package com.example.hiker.service;

import com.example.hiker.model.SavedTrail;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SavedTrailService {

    Mono<SavedTrail> saveTrail(String userId, String trailId);
    Mono<Void> unsaveTrail(String userId, String trailId);
    Flux<SavedTrail> getUserSavedTrails(String userId);
    Mono<Boolean> isTrailSavedByUser(String userId, String trailId);
}
