package com.vromanyu.upload.service;

import com.vromanyu.upload.dto.*;
import com.vromanyu.upload.entity.UserFile;
import com.vromanyu.upload.enums.UploadStatus;
import com.vromanyu.upload.exception.FileNotFoundException;
import com.vromanyu.upload.exception.FileNotUploadedException;
import com.vromanyu.upload.exception.FilesNamesNotMatchException;
import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.nio.file.Files;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
            Log.errorf("error occurred while saving file: %s", fileUploadRequest, e);
            throw new RuntimeException("error occurred while saving file", e);
        }
    }

    @Override
    public FileUploadStatusResponse getFileStatus(String fileUuid) {
        Log.infof("obtaining file with file_uuid: %s", fileUuid);
        try {
            TypedQuery<UserFile> query = entityManager.createQuery("select u from UserFile u where u.fileUuid = :fileUuid", UserFile.class);
            query.setParameter("fileUuid", fileUuid);
            UserFile userFile = query.getResultStream().findFirst().orElseThrow(() -> new FileNotFoundException(String.format("file with file_uuid: %s not found", fileUuid)));
            return new FileUploadStatusResponse(userFile.fileUuid,
                    userFile.fileName,
                    userFile.userName,
                    userFile.status,
                    userFile.uploadedAt
            );
        } catch (Exception e) {
            if (e instanceof FileNotFoundException fileNotFoundException)
                throw fileNotFoundException;
            Log.errorf("error occurred while obtaining file status: %s", fileUuid, e);
            throw new RuntimeException("error occurred while obtaining file status", e);
        }
    }

    @Override
    public AllUserFilesStatusResponse getAllUserFiles(String userName) {
        Log.infof("retrieving all files for user: %s", userName);
        TypedQuery<UserFile> query = entityManager.createQuery("select u from UserFile u where u.userName = :userName", UserFile.class);
        query.setParameter("userName", userName);
        Set<UserFileStatusResponse> userFiles = query.getResultStream()
                .map(userFile -> new UserFileStatusResponse(userFile.fileUuid, userFile.fileName, userFile.status, userFile.uploadedAt))
                .collect(Collectors.toSet());
        Log.infof("retrieved %s files for user: %s", userFiles.size(), userName);
        return new AllUserFilesStatusResponse(userName,
                userFiles.size(),
                userFiles);
    }

    @Override
    public GetFileUrlResponse getFileUrl(String fileUuid) {
        Log.infof("retrieving file url for file_uuid: %s", fileUuid);
        try {
            TypedQuery<UserFile> query = entityManager.createQuery("select u from UserFile u where u.fileUuid = :fileUuid", UserFile.class);
            query.setParameter("fileUuid", fileUuid);
            UserFile userFile = query.getResultStream().findFirst().orElseThrow(() -> new FileNotFoundException(String.format("file with file_uuid: %s not found", fileUuid)));
            if (userFile.status != UploadStatus.SUCCESS) {
                throw new FileNotFoundException(String.format("file with file_uuid: %s is not uploaded. File status: '%s'", fileUuid, userFile.status));
            }
            return new GetFileUrlResponse(userFile.fileUuid,
                    userFile.url,
                    userFile.fileName,
                    userFile.userName,
                    userFile.status,
                    userFile.uploadedAt,
                    userFile.expirationDate);

        } catch (Exception e) {
            switch (e) {
                case FileNotFoundException fileNotFoundException -> throw fileNotFoundException;
                case FileNotUploadedException fileNotUploadedException -> throw fileNotUploadedException;
                default -> {
                    Log.errorf("error occurred while obtaining file url: %s", fileUuid, e);
                    throw new RuntimeException("error occurred while obtaining file url", e);
                }
            }
        }
    }
}
