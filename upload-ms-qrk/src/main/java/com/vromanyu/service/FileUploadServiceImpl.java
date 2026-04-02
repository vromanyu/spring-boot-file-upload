package com.vromanyu.service;

import com.vromanyu.dto.FileUploadRequest;
import com.vromanyu.dto.FileUploadResponse;
import com.vromanyu.dto.UploadStatus;
import com.vromanyu.entity.UserFile;
import com.vromanyu.exception.FileNameNotMatchException;
import com.vromanyu.repository.UserFileRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.time.OffsetDateTime;
import java.util.UUID;

@ApplicationScoped
public class FileUploadServiceImpl implements FileUploadService {

    private static final Logger logger = Logger.getLogger(FileUploadServiceImpl.class);

    @Inject
    UserFileRepository userFileRepository;

    @Override
    @Transactional
    public FileUploadResponse uploadFile(FileUploadRequest fileUploadRequest) {
        logger.info("saving file: " + fileUploadRequest + " to the database");
        try {
            if (!isFileNameMatching(fileUploadRequest.fileName(), fileUploadRequest.data().fileName())) {
                throw new FileNameNotMatchException("file name does not match");
            }
            UserFile userFile = new UserFile();
            userFile.setFileUuid(UUID.randomUUID().toString());
            userFile.setFileName(fileUploadRequest.data().fileName());
            userFile.setUserName("test");
            userFile.setUploadedAt(OffsetDateTime.now());
            userFile.setFileData(Files.readAllBytes(fileUploadRequest.data().uploadedFile()));
            userFile.setStatus(UploadStatus.CREATED);
            UserFile savedFile = userFileRepository.save(userFile);
            FileUploadResponse response = new FileUploadResponse(savedFile.getFileUuid(), savedFile.getFileName(), "test", savedFile.getStatus(), savedFile.getUploadedAt());
            logger.info("file saved successfully: " + response);
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
