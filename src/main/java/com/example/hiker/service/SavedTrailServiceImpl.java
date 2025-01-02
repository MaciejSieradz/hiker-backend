package com.example.hiker.service;

import com.example.hiker.model.SavedTrail;
import com.example.hiker.repository.SavedTrailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class SavedTrailServiceImpl implements SavedTrailService {

    private final SavedTrailRepository savedTrailRepository;


    @Override
    public Mono<SavedTrail> saveTrail(String userEmail, String trailId) {
        return savedTrailRepository.findByUserEmailAndTrailId(userEmail, trailId)
                .switchIfEmpty(savedTrailRepository.save(new SavedTrail(userEmail, trailId)));
    }

    @Override
    public Mono<Void> unsaveTrail(String userEmail, String trailId) {
        return savedTrailRepository.deleteByUserEmailAndTrailId(userEmail, trailId);
    }

    @Override
    public Flux<SavedTrail> getUserSavedTrails(String userEmail) {
        return savedTrailRepository.findByUserEmail(userEmail).doOnNext(savedTrail -> log.info("Trails: {}", savedTrail));
    }

    @Override
    public Mono<Boolean> isTrailSavedByUser(String userEmail, String trailId) {
        return savedTrailRepository.findByUserEmailAndTrailId(userEmail, trailId)
                .map(_ -> true)
                .defaultIfEmpty(false);
    }
}
