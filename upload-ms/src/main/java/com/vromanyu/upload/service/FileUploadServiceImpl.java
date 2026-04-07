package com.vromanyu.upload.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.vromanyu.upload.aggregate.UserFile;
import com.vromanyu.upload.dto.*;
import com.vromanyu.upload.event.FileUploadReceivedEvent;
import com.vromanyu.upload.exception.BlobNotFoundException;
import com.vromanyu.upload.exception.FileNameNotMatchException;
import com.vromanyu.upload.exception.FileNotFoundException;
import com.vromanyu.upload.exception.FileNotUploadedException;
import com.vromanyu.upload.repository.UserFileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Transactional
public class FileUploadServiceImpl implements FileUploadService {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadServiceImpl.class);
    private final UserFileRepository userFileRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final Storage storage;
    private final BucketInfo bucketInfo = BucketInfo.newBuilder("spring-boot-file-upload-bucket").build();

    public FileUploadServiceImpl(UserFileRepository userFileRepository, ApplicationEventPublisher eventPublisher, Storage storage) {
        this.userFileRepository = userFileRepository;
        this.eventPublisher = eventPublisher;
        this.storage = storage;
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
            userFile.setUploadedAt(Instant.now());
            userFile.setFileData(data.getBytes());
            userFile.setFileContentType(data.getContentType());
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

    @Override
    public FileUrlResponse getFileUrl(String fileUuid) {
        logger.info("retrieving file url for file_uuid: {}", fileUuid);
        UserFile userFile = userFileRepository.findByFileUuid(fileUuid).orElseThrow(() -> new FileNotFoundException("file_uuid " + fileUuid + " not found"));
        if (userFile.getStatus() != UploadStatus.SUCCESS) {
            throw new FileNotUploadedException("file with file_uuid " + fileUuid + " is not uploaded yet");
        }
        if (userFile.getExpirationDate().isBefore(Instant.now())) {
            logger.info("file url expired for file_uuid: {}", fileUuid);
            Blob blob = storage.get(bucketInfo.getName(), userFile.getUserName() + "/" + userFile.getFileUuid() + "." + userFile.getFileContentType().substring(userFile.getFileContentType().indexOf("/") + 1));
            if (blob == null) {
                throw new BlobNotFoundException("file_uuid " + fileUuid + " not found");
            }
            String url = blob.signUrl(1, TimeUnit.DAYS).toString();
            logger.info("generated new file url for file_uuid: {}", fileUuid);
            Instant expirationDate = Instant.now().plus(Duration.ofDays(1));
            userFile.setUrl(url);
            userFile.setExpirationDate(expirationDate);
            userFileRepository.save(userFile);
            return new FileUrlResponse(userFile.getFileUuid(),
                    url,
                    userFile.getFileName(),
                    userFile.getUserName(),
                    userFile.getStatus(),
                    userFile.getUploadedAt(),
                    expirationDate
            );
        }
        return new FileUrlResponse(userFile.getFileUuid(),
                userFile.getUrl(),
                userFile.getFileName(),
                userFile.getUserName(),
                userFile.getStatus(),
                userFile.getUploadedAt(),
                userFile.getExpirationDate());
    }

    private boolean isFileNameMatching(String providedFileName, String actualFileName) {
        return providedFileName.equals(actualFileName);
    }
}
