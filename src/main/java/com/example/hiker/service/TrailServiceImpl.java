package com.example.hiker.service;

import com.example.hiker.dto.CommentDto;
import com.example.hiker.dto.ReviewInput;
import com.example.hiker.dto.TrailDto;
import com.example.hiker.dto.TrailInput;
import com.example.hiker.model.EstimatedHikingTime;
import com.example.hiker.model.Review;
import com.example.hiker.model.Trail;
import com.example.hiker.model.User;
import com.example.hiker.repository.SavedTrailRepository;
import com.example.hiker.repository.TrailRepository;
import com.example.hiker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrailServiceImpl implements TrailService {

    private final TrailRepository trailRepository;
    private final SavedTrailRepository savedTrailRepository;
    private final UserRepository userRepository;

    @Override
    public Flux<TrailDto> getAllTrails() {
        return trailRepository.findAll().flatMap(this::convertTrailToTrailDto);
    }

    @Override
    public Flux<TrailDto> getUserTrails(String email) {
        return trailRepository.findTrailsByUserEmail(email).flatMap(this::convertTrailToTrailDto);
    }

    @Override
    public Mono<TrailDto> getTrailById(String id, String email) {
        return trailRepository.findById(id)
                .flatMap(trail -> savedTrailRepository.findByUserEmailAndTrailId(email, id)
                        .map(_ -> true)
                        .defaultIfEmpty(false)
                        .flatMap(isSaved -> convertToTrailDto(trail, isSaved)));
    }

    @Override
    public Mono<TrailDto> createTrail(TrailInput trailInput, String email) {

        return userRepository.findByEmail(email)
                .switchIfEmpty(createDefaultUser(email))
                .flatMap(_ -> {
                    var trail = convertTrailInputToTrail(trailInput, email);
                    return trailRepository.insert(trail)
                            .flatMap(this::convertTrailToTrailDto);
                });
    }

    @Override
    public Mono<Boolean> addReview(String trailId, ReviewInput reviewInput, String email) {
        return userRepository.findByEmail(email)
                .switchIfEmpty(createDefaultUser(email))
                .flatMap(
                        user -> trailRepository
                                .findById(trailId)
                                .flatMap(trail -> {
                                    trail.getReviews().add(convertReviewInputToReview(reviewInput, user));
                                    return trailRepository.save(trail).map(trail1 -> true);
                                }));
    }

    @Override
    public Mono<Boolean> deleteTrail(String id, String email) {
        return trailRepository.findById(id).flatMap(trail -> {
            if (trail.getUserEmail().equals(email)) {
                return trailRepository.delete(trail).thenReturn(true);
            } else {
                return Mono.just(false);
            }
        });
    }

    @Override
    public Mono<TrailDto> updateTrail(String id, String email, TrailInput trailInput) {
        return trailRepository.findById(id).flatMap(trail -> {
            if (trailInput.title() != null) trail.setTitle(trailInput.title());
            if (trailInput.difficulty() != null) trail.setDifficulty(trailInput.difficulty());
            if (trailInput.estimatedHikingTime() != null) {
                trail.setEstimatedHikingTime(
                        new EstimatedHikingTime(
                                trailInput.estimatedHikingTime().hours(),
                                trailInput.estimatedHikingTime().minutes())
                );
            }
            if (trailInput.description() != null) trail.setDescription(trailInput.description());
            if (trailInput.gpxTrailUrl() != null) trail.setGpxUrl(trailInput.gpxTrailUrl());
            if (!trailInput.photosUrl().isEmpty()) trail.setPhotosUrl(trailInput.photosUrl());
            trail.setElevationGain(trailInput.elevation());
            trail.setMaxHeight(trailInput.maxHeight());
            trail.setDistance(trailInput.distance());

            return trailRepository.save(trail);
        }).flatMap(this::convertTrailToTrailDto);
    }

    private Mono<User> createDefaultUser(String email) {
        User newUser = new User();
        newUser.setEmail(email);

        return userRepository.save(newUser);
    }

    private Review convertReviewInputToReview(ReviewInput reviewInput, User author) {
        Review review = new Review();
        review.setRating(reviewInput.mark());
        review.setComment(reviewInput.description());
        review.setCreatedAt(Instant.now());
        review.setAuthorEmail(author.getEmail());
        review.setPhotosUrl(reviewInput.photosUrl());
        return review;
    }

    private Trail convertTrailInputToTrail(TrailInput trailInput, String email) {
        var trail = new Trail();
        trail.setTitle(trailInput.title());
        trail.setDifficulty(trailInput.difficulty());
        trail.setDescription(trailInput.description());
        trail.setGpxUrl(trailInput.gpxTrailUrl());
        trail.setPhotosUrl(trailInput.photosUrl());
        trail.setEstimatedHikingTime(new EstimatedHikingTime(trailInput.estimatedHikingTime().hours(), trailInput.estimatedHikingTime().minutes()));
        trail.setDistance(trailInput.distance());
        trail.setElevationGain(trailInput.elevation());
        trail.setMaxHeight(trailInput.maxHeight());
        trail.setPhotosUrl(trailInput.photosUrl());
        trail.setUserEmail(email);
        return trail;
    }

    private Mono<TrailDto> convertToTrailDto(Trail trail, Boolean isMarked) {
        return Flux.fromIterable(trail.getReviews())
                .flatMap(review -> userRepository.findByEmail(review.getAuthorEmail())
                        .map(user -> new CommentDto(
                                user.getAvatarUrl(),
                                user.getUsername(),
                                review.getRating(),
                                review.getCreatedAt().toString(),
                                review.getComment(),
                                review.getPhotosUrl()
                        )))
                .collectList()
                .map(comments -> TrailDto.builder()
                        .id(trail.getId())
                        .title(trail.getTitle())
                        .difficulty(trail.getDifficulty())
                        .distance(trail.getDistance())
                        .elevationGain(trail.getElevationGain())
                        .maxHeight(trail.getMaxHeight())
                        .description(trail.getDescription())
                        .estimatedHikingTime(trail.getEstimatedHikingTime())
                        .photos(trail.getPhotosUrl())
                        .gpxUrl(trail.getGpxUrl())
                        .rating(ratingFromComments(comments))
                        .numberOfRatings(comments.size())
                        .isMarked(isMarked)
                        .comments(comments)
                        .build());
    }

    private Mono<TrailDto> convertTrailToTrailDto(Trail trail) {
        return Flux.fromIterable(trail.getReviews())
                .flatMap(review -> userRepository.findByEmail(review.getAuthorEmail())
                        .map(user -> new CommentDto(
                                user.getAvatarUrl(),
                                user.getUsername(),
                                review.getRating(),
                                review.getCreatedAt().toString(),
                                review.getComment(),
                                review.getPhotosUrl()
                        )))
                .collectList()
                .map(comments -> TrailDto.builder()
                        .id(trail.getId())
                        .title(trail.getTitle())
                        .difficulty(trail.getDifficulty())
                        .distance(trail.getDistance())
                        .elevationGain(trail.getElevationGain())
                        .maxHeight(trail.getMaxHeight())
                        .description(trail.getDescription())
                        .estimatedHikingTime(trail.getEstimatedHikingTime())
                        .photos(trail.getPhotosUrl())
                        .gpxUrl(trail.getGpxUrl())
                        .rating(ratingFromComments(comments))
                        .numberOfRatings(comments.size())
                        .comments(comments)
                        .build())
                .doOnNext(trailDto -> log.info("Trail: {}", trailDto));
    }

    private double ratingFromComments(List<CommentDto> comments)  {
        var numberOfComments = comments.size();
        if (numberOfComments == 0) return 0;
        return (double) comments.stream().map(CommentDto::rating).mapToInt(Integer::intValue).sum() / numberOfComments;
    }
}
