package com.example.hiker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/buckets")
@RequiredArgsConstructor
public class BucketController {

    private final S3Client s3Client;

    @GetMapping
    private List<String> listBuckets() {
        var buckets = s3Client.listBuckets();

        return buckets.buckets().stream().map(Bucket::name).collect(Collectors.toList());
    }

}
