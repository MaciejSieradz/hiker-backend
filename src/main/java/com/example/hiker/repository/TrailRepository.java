package com.example.hiker.repository;

import com.example.hiker.model.Trail;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface TrailRepository extends ReactiveMongoRepository<Trail,String> {
    Flux<Trail> findTrailsByUserEmail(String email);
}
