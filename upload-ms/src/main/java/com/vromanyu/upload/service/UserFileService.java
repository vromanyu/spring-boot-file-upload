package com.vromanyu.upload.service;

import com.vromanyu.upload.dto.FileUploadRequest;
import com.vromanyu.upload.dto.FileUploadResponse;
import com.vromanyu.upload.dto.UploadStatus;
import com.vromanyu.upload.entity.UserFile;
import com.vromanyu.upload.exception.FileNameNotMatchException;
import com.vromanyu.upload.repository.UserFileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@Transactional
public class UserFileService implements FileUploadService {

    private static final Logger logger = LoggerFactory.getLogger(UserFileService.class);
    private final UserFileRepository userFileRepository;

    public UserFileService(UserFileRepository userFileRepository) {
        this.userFileRepository = userFileRepository;
    }

    @Override
    public FileUploadResponse uploadFile(FileUploadRequest fileUploadRequest) {
        logger.info("saving file: {} to the database", fileUploadRequest);
        MultipartFile data = fileUploadRequest.data();
        try {
            if (!isFileNameMatching(fileUploadRequest.fileName(), data.getOriginalFilename())) {
                throw new FileNameNotMatchException("file name does not match");
            }
            UserFile userFile = new UserFile();
            userFile.setFileUuid(UUID.randomUUID().toString());
            userFile.setFileName(data.getOriginalFilename());
            userFile.setUserName("test");
            userFile.setUploadedAt(OffsetDateTime.now());
            userFile.setFileData(data.getBytes());
            userFile.setStatus(UploadStatus.CREATED);
            UserFile savedFile = userFileRepository.save(userFile);
            FileUploadResponse response = new FileUploadResponse(savedFile.getFileUuid(), savedFile.getFileName(), "test", savedFile.getStatus(), savedFile.getUploadedAt());
            logger.info("file saved successfully: {}", response);
            return response;
        } catch (IOException e) {
            logger.error("error occurred while saving file", e);
            throw new RuntimeException("error occurred while saving file", e);
        }
    }

    private boolean isFileNameMatching(String providedFileName, String actualFileName) {
        return providedFileName.equals(actualFileName);
    }
}
