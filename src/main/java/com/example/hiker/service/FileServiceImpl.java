package com.example.hiker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

    private final S3Client s3Client;

    @Value("${aws-url}")
    private String awsUrl;

    @Override
    public Mono<String> uploadFileToS3(FilePart filePart) {
        return Mono.fromCallable(() ->
                        Files.createTempFile("upload-", filePart.filename()))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(tempPath ->
                        filePart.transferTo(tempPath.toFile())
                                .then(Mono.fromCallable(() -> {
                                    PutObjectRequest request = PutObjectRequest.builder()
                                            .bucket("hiker")
                                            .key(filePart.filename())
                                            .build();

                                    s3Client.putObject(request, RequestBody.fromFile(tempPath.toFile()));
                                    return awsUrl + filePart.filename();
                                }).subscribeOn(Schedulers.boundedElastic()))
                                .doFinally(signalType -> {
                                    log.info("Uploaded file {}", filePart.filename());
                                    Mono.fromRunnable(() -> {
                                                try {
                                                    Files.delete(tempPath);
                                                } catch (IOException e) {
                                                    log.error("Failed to delete temp file: {}", tempPath, e);
                                                }
                                            }).subscribeOn(Schedulers.boundedElastic())
                                            .subscribe();
                                })
                );
    }

    @Override
    public Flux<String> uploadImagesToS3(List<FilePart> fileParts) {
        return Flux.fromStream(fileParts.stream().map(this::uploadFileToS3)).flatMap(mono -> mono);
    }
}
