package com.example.hiker.controller;

import com.example.hiker.dto.TrailDto;
import com.example.hiker.service.TrailServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class TrailGraphQLControllerTest {

    @Mock
    private TrailServiceImpl trailService;

    @InjectMocks
    private TrailGraphQLController trailGraphQLController;

    @Test
    void getAllTrails() {

        var testData = List.of(
                TrailDto.builder().id("testowy id").description("Piekna wycieczka").title("Wycieczka do Zakopca").build(),
                TrailDto.builder().id("testowy id 2").description("Piekna wycieczka").title("Wycieczka do Zakopca").build(),
                TrailDto.builder().id("testowy id 2").description("Piekna wycieczka").title("Wycieczka do Zakopca").build()
        );

        Mockito.when(trailService.getAllTrails()).thenReturn(
                Flux.fromIterable(
                        List.of(
                                TrailDto.builder().id("testowy id").description("Piekna wycieczka").title("Wycieczka do Zakopca").build(),
                                TrailDto.builder().id("testowy id 2").description("Piekna wycieczka").title("Wycieczka do Zakopca").build(),
                                TrailDto.builder().id("testowy id 2").description("Piekna wycieczka").title("Wycieczka do Zakopca").build()
                        )
                )
        );

        assertTrue(testData.containsAll(Objects.requireNonNull(trailGraphQLController.getAllTrails().collectList().block())));
    }

    @Test
    void getTrailById() {}
}