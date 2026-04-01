package com.vromanyu.upload.controller;

import com.vromanyu.upload.dto.AllUserFilesResponse;
import com.vromanyu.upload.dto.FileUploadStatusResponse;
import com.vromanyu.upload.service.FileUploadStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v{version}")
public class FileStatusController {

    private static final Logger logger = LoggerFactory.getLogger(FileStatusController.class);
    private final FileUploadStatusService fileUploadStatusService;

    public FileStatusController(FileUploadStatusService fileUploadStatusService) {
        this.fileUploadStatusService = fileUploadStatusService;
    }

    @GetMapping("/upload/status/{fileUuid}")
    public ResponseEntity<FileUploadStatusResponse> getFileStatus(@PathVariable String fileUuid) {
        logger.info("received file status request for file_uuid: {}", fileUuid);
        FileUploadStatusResponse response = fileUploadStatusService.getFileStatus(fileUuid);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/upload/status/all")
    public ResponseEntity<AllUserFilesResponse> getAllUserFiles() {
        logger.info("received retrieve request for user: 'test'");
        AllUserFilesResponse response = fileUploadStatusService.getAllUserFiles("test");
        return ResponseEntity.ok(response);
    }
}
