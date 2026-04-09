package com.vromanyu.upload.service;

import com.vromanyu.upload.dto.*;
import com.vromanyu.upload.entity.UserFile;
import com.vromanyu.upload.enums.UploadStatus;
import com.vromanyu.upload.exception.FilesNamesNotMatchException;
import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.nio.file.Files;
import java.time.Instant;
import java.util.UUID;

@ApplicationScoped
public class FileUploadServiceImpl implements FileUploadService {

    @Inject
    EntityManager entityManager;

    public void init(@Observes StartupEvent startupEvent) {
        Log.infof("Initializing %s", this.getClass().getSimpleName());
    }

    @Override
    @Transactional
    public FileUploadResponse uploadFile(FileUploadRequest fileUploadRequest) {
        Log.infof("saving file: %s to the database", fileUploadRequest);
        FileUpload fileData = fileUploadRequest.data();
        try {
            if (!fileData.fileName().equalsIgnoreCase(fileUploadRequest.fileName())) {
                throw new FilesNamesNotMatchException(String.format("actual file name: '%s' and provided file name: '%s' do not match", fileData.fileName(), fileUploadRequest.fileName()));
            }
            UserFile userFile = new UserFile();
            userFile.fileUuid = UUID.randomUUID().toString();
            userFile.fileName = fileData.fileName();
            userFile.userName = "test";
            userFile.uploadedAt = Instant.now();
            userFile.fileData = Files.readAllBytes(fileData.filePath());
            userFile.fileContentType = fileData.contentType();
            userFile.status = UploadStatus.CREATED;
            entityManager.persist(userFile);
            Log.infof("file saved successfully: %s", userFile);
            return new FileUploadResponse(userFile.fileUuid,
                    userFile.fileName,
                    userFile.userName,
                    userFile.status,
                    userFile.uploadedAt);
        } catch (Exception e) {
            Log.errorf("error occurred while saving file: %s", fileUploadRequest, e);
            if (e instanceof FilesNamesNotMatchException filesNamesNotMatchException)
                throw filesNamesNotMatchException;
            throw new RuntimeException("error occurred while saving file", e);
        }
    }

    @Override
    public FileUploadStatusResponse getFileStatus(String fileUuid) {
        throw new UnsupportedOperationException();
    }

    @Override
    public AllUserFilesStatusResponse getAllUserFiles(String userName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public GetFileUrlResponse getFileUrl(String fileUuid) {
        throw new UnsupportedOperationException();
    }
}
