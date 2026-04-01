package com.vromanyu.upload.controller;

import com.vromanyu.upload.dto.FileUploadRequest;
import com.vromanyu.upload.dto.FileUploadResponse;
import com.vromanyu.upload.service.FileUploadService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v{version}")
public class FileUploadController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);
    private final FileUploadService fileUploadService;

    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping("/upload")
    public ResponseEntity<FileUploadResponse> upload(@Valid @ModelAttribute FileUploadRequest fileUploadRequest) {
        logger.info("received file upload request: {}", fileUploadRequest);
        FileUploadResponse response = fileUploadService.uploadFile(fileUploadRequest);
        return ResponseEntity.ok(response);
    }
}
