package com.vromanyu.upload.service;


import com.vromanyu.upload.dto.*;

public interface FileUploadService {
    FileUploadResponse uploadFile(FileUploadRequest fileUploadRequest);

    FileUploadStatusResponse getFileStatus(String fileUuid);

    AllUserFilesResponse getAllUserFiles(String userName);

    FileUrlResponse getFileUrl(String fileUuid);
}
