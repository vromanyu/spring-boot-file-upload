package com.vromanyu.upload.service;

import com.vromanyu.upload.dto.*;
import com.vromanyu.upload.entity.UserFile;
import com.vromanyu.upload.event.FileUploadReceivedEvent;
import com.vromanyu.upload.exception.FileNameNotMatchException;
import com.vromanyu.upload.exception.FileNotFoundException;
import com.vromanyu.upload.repository.UserFileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class FileUploadServiceImpl implements FileUploadService {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadServiceImpl.class);
    private final UserFileRepository userFileRepository;
    private final ApplicationEventPublisher eventPublisher;

    public FileUploadServiceImpl(UserFileRepository userFileRepository, ApplicationEventPublisher eventPublisher) {
        this.userFileRepository = userFileRepository;
        this.eventPublisher = eventPublisher;
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
            logger.info("publishing file upload event");
            FileUploadReceivedEvent fileUploadReceivedEvent = new FileUploadReceivedEvent(savedFile.getFileUuid(), savedFile.getFileName(), savedFile.getFileData(), savedFile.getUserName(), savedFile.getUploadedAt());
            eventPublisher.publishEvent(fileUploadReceivedEvent);
            return response;
        } catch (IOException e) {
            logger.error("error occurred while saving file", e);
            throw new RuntimeException("error occurred while saving file", e);
        }
    }

    @Override
    public FileUploadStatusResponse getFileStatus(String fileUuid) {
        logger.info("obtaining file with file_uuid: {}", fileUuid);
        Optional<UserFile> optionalUserFile = userFileRepository.findByFileUuid(fileUuid);
        if (optionalUserFile.isEmpty())
            throw new FileNotFoundException("file_uuid " + fileUuid + " not found");
        UserFile userFile = optionalUserFile.get();
        logger.info("file found: {}", userFile);
        return new FileUploadStatusResponse(userFile.getFileUuid(),
                userFile.getFileName(),
                userFile.getUserName(),
                userFile.getStatus(),
                userFile.getUploadedAt());
    }

    @Override
    public AllUserFilesResponse getAllUserFiles(String userName) {
        logger.info("retrieving all files for user: '{}'", userName);
        Set<UserFileResponse> allUserFiles = userFileRepository
                .findAllByUserName(userName)
                .stream()
                .map(file -> new UserFileResponse(file.getFileUuid(), file.getFileName(), file.getStatus(), file.getUploadedAt()))
                .collect(Collectors.toSet());
        logger.info("retrieved {} files for user: '{}'", allUserFiles.size(), userName);
        return new AllUserFilesResponse(userName, allUserFiles.size(), allUserFiles);
    }

    private boolean isFileNameMatching(String providedFileName, String actualFileName) {
        return providedFileName.equals(actualFileName);
    }
}
