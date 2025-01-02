package com.example.hiker.controller;

import com.example.hiker.dto.TrailDto;
import com.example.hiker.service.FileService;
import com.example.hiker.service.TrailServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
public class TrailController {

    private final TrailServiceImpl trailService;
    private final FileService fileService;

    @GetMapping
    Flux<TrailDto> trails() {
        return trailService.getAllTrails();
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<String>> gpxFileUpload(@RequestPart("file") FilePart file) {
        return fileService.uploadFileToS3(file)
                .map(result -> ResponseEntity.ok().body(result))
                .onErrorResume(e -> Mono.just(ResponseEntity.internalServerError().body("Failed to upload file")));
    }

    @PostMapping(value = "/upload-images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<List<String>>> uploadImages(@RequestPart("files") List<FilePart> files) {
        return fileService.uploadImagesToS3(files).collectList().map(ResponseEntity::ok);
    }
}
