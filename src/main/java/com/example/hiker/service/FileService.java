package com.example.hiker.service;

import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface FileService {

    Mono<String> uploadFileToS3(FilePart filePart);

    Flux<String> uploadImagesToS3(List<FilePart> fileParts);
}
