package com.vromanyu.service;

import com.vromanyu.annotations.DatabaseStorage;
import com.vromanyu.dto.*;
import com.vromanyu.entity.UserFile;
import com.vromanyu.exception.FileNameNotMatchException;
import com.vromanyu.exception.FileNotFoundException;
import com.vromanyu.repository.UserFileRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
@DatabaseStorage
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
            userFile.setUploadedAt(OffsetDateTime.now(ZoneOffset.UTC));
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

    @Override
    @Transactional
    public FileUploadStatusResponse getFileStatus(String fileUuid) {
        logger.info("obtaining file with file_uuid: " + fileUuid);
        UserFile userFile = userFileRepository.findByFileUuid(fileUuid).orElseThrow(() -> new FileNotFoundException("file with file_uuid " + fileUuid + " not found"));
        logger.info("file found: " + userFile);
        return new FileUploadStatusResponse(userFile.getFileUuid(), userFile.getFileName(), userFile.getUserName(), userFile.getStatus(), userFile.getUploadedAt());
    }

    @Override
    @Transactional
    public AllUserFilesResponse getAllUserFiles(String userName) {
        logger.info("retrieving all files for user: '" + userName + "'");
        Set<UserFile> allUserFiles = userFileRepository.findAllByUserName(userName);
        int totalFiles = allUserFiles.size();
        logger.info("found " + totalFiles + " files for user: '" + userName + "'");
        Set<UserFileResponse> userFileResponse = allUserFiles.stream().map(f -> new UserFileResponse(f.getFileUuid(), f.getFileName(), f.getStatus(), f.getUploadedAt())).collect(Collectors.toSet());
        return new AllUserFilesResponse(userName, totalFiles, userFileResponse);
    }

    private boolean isFileNameMatching(String providedFileName, String actualFileName) {
        return providedFileName.equals(actualFileName);
    }
}
