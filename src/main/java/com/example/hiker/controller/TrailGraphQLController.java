package com.example.hiker.controller;

import com.example.hiker.dto.ReviewInput;
import com.example.hiker.dto.TrailDto;
import com.example.hiker.dto.TrailInput;
import com.example.hiker.service.SavedTrailService;
import com.example.hiker.service.TrailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TrailGraphQLController {

    private final TrailService trailService;
    private final SavedTrailService savedTrailService;

    @QueryMapping("getAllTrails")
    public Flux<TrailDto> getAllTrails() {
        log.info("Getting all trails");
        return trailService.getAllTrails();
    }

    @QueryMapping
    public Mono<TrailDto> getTrailById(@Argument String id, Authentication authentication) {
        var email = retrieveEmailFromAuthentication(authentication);
        return trailService.getTrailById(id, email);
    }

    @QueryMapping
    public Flux<TrailDto> getSavedTrails(Authentication authentication) {
        var email = retrieveEmailFromAuthentication(authentication);
        return savedTrailService.getUserSavedTrails(email)
                .flatMap(savedTrail -> trailService.getTrailById(savedTrail.getTrailId(), email));
    }

    @QueryMapping
    public Flux<TrailDto> getUserTrails(Authentication authentication) {
        var email = retrieveEmailFromAuthentication(authentication);
        return trailService.getUserTrails(email);
    }

    @MutationMapping
    public Mono<TrailDto> createTrail(@Argument TrailInput trailInput, Authentication authentication) {
        var email = retrieveEmailFromAuthentication(authentication);
        return trailService.createTrail(trailInput, email);
    }

    @MutationMapping
    public Mono<Boolean> deleteTrail(@Argument String id, Authentication authentication) {
        var email = retrieveEmailFromAuthentication(authentication);
        return trailService.deleteTrail(id, email);
    }

    @MutationMapping
    public Mono<TrailDto> updateTrail(@Argument String id, @Argument TrailInput trailInput, Authentication authentication) {
        var email = retrieveEmailFromAuthentication(authentication);
        return trailService.updateTrail(id, email, trailInput);
    }

    @MutationMapping("addReview")
    public Mono<Boolean> addReview(@Argument String trailId, @Argument ReviewInput reviewInput, Authentication authentication) {
        var email = retrieveEmailFromAuthentication(authentication);

        return trailService.addReview(trailId, reviewInput, email);
    }

    @MutationMapping
    public Mono<Boolean> markTrailAsSaved(@Argument String trailId, Authentication authentication) {
        var email = retrieveEmailFromAuthentication(authentication);
        return savedTrailService.saveTrail(email, trailId)
                .map(_ -> true)
                .defaultIfEmpty(false);
    }

    @MutationMapping
    public Mono<Boolean> unmarkTrail(@Argument String trailId, Authentication authentication) {
        var email = retrieveEmailFromAuthentication(authentication);
        return savedTrailService.unsaveTrail(email, trailId).thenReturn(true);
    }

    private String retrieveEmailFromAuthentication(Authentication authentication) {
        var jwt = (Jwt) authentication.getPrincipal();
        return jwt.getClaimAsString("email");
    }
}
