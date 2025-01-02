package com.example.hiker.repository;

import com.example.hiker.model.SavedTrail;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SavedTrailRepository extends ReactiveCrudRepository<SavedTrail, Long> {
    Flux<SavedTrail> findByUserEmail(String userId);
    Mono<SavedTrail> findByUserEmailAndTrailId(String userId, String trailId);
    Mono<Void> deleteByUserEmailAndTrailId(String userId, String trailId);
}
