package com.vromanyu.upload.service;

import com.google.cloud.storage.Storage;
import com.vromanyu.upload.aggregate.UserFile;
import com.vromanyu.upload.dto.UploadStatus;
import com.vromanyu.upload.event.FileUploadReceivedEvent;
import com.vromanyu.upload.exception.FileNotFoundException;
import com.vromanyu.upload.repository.UserFileRepository;
import com.vromanyu.upload.utility.BlobUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Duration;
import java.time.Instant;

@Component
public class FileUploadProcessorImpl implements FileUploadProcessor {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadProcessorImpl.class);
    private final UserFileRepository userFileRepository;
    private final Storage storage;

    public FileUploadProcessorImpl(UserFileRepository userFileRepository, Storage storage) {
        this.userFileRepository = userFileRepository;
        this.storage = storage;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Async
    @TransactionalEventListener
    public void processFileUpload(FileUploadReceivedEvent fileUploadReceivedEvent) {
        logger.info("received file upload event: {}", fileUploadReceivedEvent);
        UserFile userFile = userFileRepository.findByFileUuid(fileUploadReceivedEvent.fileUuid()).orElseThrow(() -> new FileNotFoundException("file with file_uuid " + fileUploadReceivedEvent.fileUuid() + " not found"));
        if (userFile.getStatus() != UploadStatus.CREATED) {
            logger.info("file with file_uuid {} is not in CREATED state, skipping processing", userFile.getFileUuid());
            return;
        }
        logger.info("file found, processing file");
        String blobUrl = BlobUtilities.uploadBlobAndGetUrl(storage, userFile);
        Instant expirationDate = Instant.now().plus(Duration.ofDays(1));
        logger.info("updating row {}", userFile.getId());
        userFile.setFileData(null);
        userFile.setStatus(UploadStatus.SUCCESS);
        userFile.setUrl(blobUrl);
        userFile.setExpirationDate(expirationDate);
        userFileRepository.save(userFile);
        logger.info("file processed successfully");
    }
}
