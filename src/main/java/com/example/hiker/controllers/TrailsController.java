package com.example.hiker.controllers;

import com.example.hiker.dtos.TrailDTO;
import com.example.hiker.services.TrailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/trail")
@RequiredArgsConstructor
public class TrailsController {

    private final TrailService trailService;

    @GetMapping("/trails")
    public Flux<TrailDTO> trails() {
        return Flux.empty();
    }

    @GetMapping()
    public Mono<TrailDTO> getTrail(@RequestParam int id) {
        return Mono.empty();
    }
}
