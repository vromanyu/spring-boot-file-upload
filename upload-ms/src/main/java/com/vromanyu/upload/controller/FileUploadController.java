package com.vromanyu.upload.controller;

import com.vromanyu.upload.dto.AllUserFilesResponse;
import com.vromanyu.upload.dto.FileUploadRequest;
import com.vromanyu.upload.dto.FileUploadResponse;
import com.vromanyu.upload.dto.FileUploadStatusResponse;
import com.vromanyu.upload.service.FileUploadService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(value = "/upload/{fileUuid}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FileUploadStatusResponse> getFileStatus(@PathVariable String fileUuid) {
        logger.info("received file status request for file_uuid: {}", fileUuid);
        FileUploadStatusResponse response = fileUploadService.getFileStatus(fileUuid);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/upload/all/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AllUserFilesResponse> getAllUserFiles(Authentication authentication) {
        String userName = authentication.getName();
        logger.info("received get all user files request for user: '{}'", userName);
        AllUserFilesResponse response = fileUploadService.getAllUserFiles(userName);
        return ResponseEntity.ok(response);
    }
}
